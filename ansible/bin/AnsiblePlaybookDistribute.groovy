#!/usr/bin/env groovy

import java.io.*
import java.nio.channels.FileChannel
import java.nio.file.*;
import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry


def cli = new CliBuilder(usage: 'ansible-playbook-distribute.groovy [-h] [-p playbook-dir] [-e environment-file] -r role[,role[,...]')
cli.with {
	e longOpt: 'environment-file', 'The location of a file that defines environment variables used by the playbook', args: 1, type: String, required: false
	h longOpt: 'help', 'Print usage information', required: false
	i longOpt: 'ignore-files', 'If set, files matched by entries in the same directory\'s .gitignore files will be omitted.', required: false
	p longOpt: 'playbook-directory', 'The playbook directory that contains the roles to be distributed', args: 1, type: String, required: false
	r longOpt: 'roles', 'A comma-separated list of roles to be distributed in their own playbook', args: 1, type: String, required: true
}

def opts = cli.parse(args)
if (!opts) return

def args = opts.arguments()
if (opts.h || !args.isEmpty()) {
	cli.usage()
	return
} 

def environmentFile = opts.e ? Paths.get(opts.e) : null

def ignoreFiles = opts.i

def playbookDirectory = Paths.get(opts.p ?: '.')
if (!Files.isDirectory(playbookDirectory)) {
	System.err.println("Error: The playbook directory does not exist: $playbookDirectory")
	return 1
}

def resolveRoleDependencies(rolesDirectory, name, roleDependencies) {
	def ROLE_DEFINITION_MATCHER = ~/\s*-\s*\{\s*role:\s*'([0-9a-zA-Z-_]+)'.*/

	def roleDirectory = rolesDirectory.resolve(name)
	if (!Files.isDirectory(roleDirectory)) {
		System.err.println("Error: The role directory does not exist: $roleDirectory")
		return 1
	}

	def roleDependenciesFile = roleDirectory.resolve('meta/main.yml')
	if (!Files.exists(roleDependenciesFile)) return []

	roleDependenciesFile.eachLine { line ->
		def matcher = line =~ ROLE_DEFINITION_MATCHER
		if (matcher.matches()) {
			def roleDependencyName = matcher[0][1]
			if (!roleDependencies.find { it.name == roleDependencyName }) {
				roleDependencies << [ name: roleDependencyName, line: line.trim() ]
				resolveRoleDependencies(rolesDirectory, roleDependencyName, roleDependencies)
			}
		}
	}
}

def rolesDirectory = playbookDirectory.resolve("roles")
def roles = opts.r.split(',')

def roleDependencies = []
roles.each { name ->
	roleDependencies << [ name: name, line: "- { role: $name }" ]
	resolveRoleDependencies(rolesDirectory, name, roleDependencies)
}


def IGNORED_FILES = [".DS_Store", ".git", ".gitignore", ".svn"]
def ZIP_FILE_NAME = "playbook.zip"
def zipFileInputRoles = roleDependencies.collect { it.name }

def writeFileToZipFile(def zipFile, def zipEntryName, def file) {
	zipFile.putNextEntry(new ZipEntry(zipEntryName))

	def bytes = Files.readAllBytes(file)
	zipFile.write(bytes, 0, bytes.length)
	zipFile.closeEntry()
}

def writeContentToZipFile(def zipFile, def zipEntryName, String content) {
	zipFile.putNextEntry(new ZipEntry(zipEntryName))

	def bytes = content.getBytes()
	zipFile.write(bytes, 0, bytes.length)
	zipFile.closeEntry()
}

ZipOutputStream zipFile = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(ZIP_FILE_NAME)))
zipFile.setLevel(9)

// include all role dependencies
zipFileInputRoles.each { role ->
	def roleDirectory = rolesDirectory.resolve(role)

	roleDirectory.eachFileRecurse { file ->
		def ignoredFiles = []
		ignoredFiles.addAll(IGNORED_FILES)

		if (ignoreFiles) {
			def ignoreFile = file.getParent().resolve(".gitignore")
			if (Files.exists(ignoreFile)) {
				ignoreFile.eachLine {
					ignoredFiles << it
				}
			}
		}

		if (!Files.isDirectory(file)) {
			if (ignoredFiles.contains('*') || ignoredFiles.contains(file.getFileName().toString())) {
				println "Ignoring file " + file.toAbsolutePath().toString()
			} else {
				println "Packaging " + file.toAbsolutePath().toString() + "..."
				writeFileToZipFile(zipFile, playbookDirectory.relativize(file).toString(), file)
			}
		}
	}
}

// include the playbook specific tasks
def tasks = []

def tasksDirectory = playbookDirectory.resolve("tasks")
if (Files.exists(tasksDirectory)) {
	tasksDirectory.eachFile { file ->
		println "Packaging " + file.toAbsolutePath().toString() + "..."

		def taskFile = playbookDirectory.relativize(file).toString()
		tasks << taskFile
		writeFileToZipFile(zipFile, taskFile, file)
	}
}

// include the playbook environment file
if (environmentFile && Files.exists(environmentFile)) {
	writeFileToZipFile(zipFile, "set-install-env.sh", environmentFile)
}

// include the playbook.yml
def playbookRolesDefinitions = roles.collect { name ->
	"    - { role: $name }"
}.join("\r\n")

def playbookTasksDefinitions = tasks.collect { name ->
	"    - include: $name"
}.join("\r\n")

def playbookRolesInclusion = ""
if (playbookRolesDefinitions.size() > 0) {
	playbookRolesInclusion = """  roles:
$playbookRolesDefinitions
"""
}

def playbookTasksInclusion = ""
if (playbookTasksDefinitions.size() > 0) {
	playbookTasksInclusion = """  tasks:
$playbookTasksDefinitions
"""
}

def playbookContent = """---
- hosts: all
$playbookRolesInclusion
$playbookTasksInclusion
  remote_user: deploy
"""

println "Packaging playbook.yml..."
println playbookContent

writeContentToZipFile(zipFile, "playbook.yml", playbookContent)

zipFile.close()
