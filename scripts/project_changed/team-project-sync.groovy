/* :name=Team Project Sync :description=Removes extra files from source directory mappings
 *
 * @author  Briac Pilpre, Manuel Souto
 * @date    2020-05-12 (creation)
 * @date    2023-01-04 (modification)
 * @version 0.2
 */

import static org.omegat.core.events.IProjectEventListener.PROJECT_CHANGE_TYPE.*
import static javax.swing.JOptionPane.*
import static org.omegat.util.Platform.*
import org.omegat.core.Core;
import org.omegat.util.FileUtil;
import org.apache.commons.io.FileUtils;
import java.nio.file.Path;
import gen.core.project.RepositoryDefinition;
import gen.core.project.RepositoryMapping;
import org.omegat.core.team2.RemoteRepositoryProvider;

def skipSync(eventType) {
    if (!eventType.metaClass.hasProperty(eventType, 'skipSync')) {
        eventType.metaClass.skipSync = false
    }
    eventType.skipSync
}

if (eventType == LOAD) {
    // Skip sync
    if (skipSync(LOAD)) {
		LOAD.skipSync = false // reset the flag
		return
    }

	BACKUP_DIR = "source_backup";
	VERBOSE = true;

	// abort if a team project is not opened
	def props = project.projectProperties
	if (!props || !props.repositories) {
		final def title = 'Team Project Sync';
		// @todo: does this actually check that the project is a team project?
		final def msg = 'No team project opened.';
		// showMessageDialog(null, msg, title, INFORMATION_MESSAGE);
		return;
	}

	diffSourceRemoteLocal(props);
	console.println("Sync done.")
}

def diffSourceRemoteLocal(props) throws Exception {

	// @todo: this only handles files, not folders. any subfolders get empty but remain...
	
	File localSourceDir = props.getSourceDir().getAsFile();
	def projectRoot = props.projectRootDir;

	// List of current local files
	List<String> localFiles = FileUtil.buildRelativeFilesList(localSourceDir, null, ['.gitkeep']); /* includes, excludes */

	if (VERBOSE) {
		console.println("--- Current local files ---");
		for (String s : localFiles) {
			console.println(s);
		}
	}

	// Build the list of remote source files
	def remoteFiles = new ArrayList<>(localFiles.size());

	// this line because it seems hidden remote files are not collected
	/* remoteFiles.add('.gitkeep') */

	for (def repoDefinition in props.repositories) {
		def repositoryDir = getRepositoryDir(projectRoot, repoDefinition);

		for (def repoMapping in repoDefinition.getMapping()) {
			// Only look at source directory mappings
			if (repoMapping.getLocal().startsWith(localSourceDir.getName())) {
				def from = new File(repositoryDir, withoutLeadingSlash(repoMapping.getRepository()));
				def remoteSourceFiles = FileUtil.buildRelativeFilesList(from, null, null);
				for (def remoteFile in remoteSourceFiles) {
					// Build the mapped path name as it will be found in the local directory
					def targetPath = new File(new File(projectRoot, repoMapping.getLocal()), remoteFile).toPath();
					def mappedLocal = localSourceDir.toPath().relativize(targetPath).toString().replace('\\', '/');
					remoteFiles.add(mappedLocal);
				}
			}
		}
	}


	if (VERBOSE) {
		console.println("--- Current remote files ---");
		for (String s : remoteFiles) {
			console.println(s);
		}
	}

	if (VERBOSE) console.println("--- Files to be removed from source ---");
	localFiles.removeAll(remoteFiles);

	// def backupDir = new File(projectRoot, BACKUP_DIR);
	// backupDir.mkdirs();
	def hasChanged = false;
	for (String s : localFiles) {
		console.println(s);
		try {
			// FileUtils.moveFile(new File(localSourceDir, s), new File(backupDir, s));
			// no need for backups, old files can be deleted // @todo: option to choose delete or backup
			new File(localSourceDir, s).delete()
			hasChanged = true;
		} catch (Exception e) {
			console.println(e);
		}
	}

	if (hasChanged && eventType == LOAD) {
		reloadProjectOnetime()
		// org.omegat.gui.main.ProjectUICommands.projectReload();
	}
}

def getRepositoryDir(projectRoot, repo) {
	def path = repo.getUrl().replaceAll("[^A-Za-z0-9\\.]", "_").replaceAll("__+", "_");
	return new File(new File(projectRoot, RemoteRepositoryProvider.REPO_SUBDIR), path);
}

def withoutLeadingSlash(s) {
	return s.startsWith("/") ? s.substring(1) : s;
}

def reloadProjectOnetime() {
    LOAD.skipSync = true    // avoid potentially infinity reloading loop
    javax.swing.SwingUtilities.invokeLater({
        org.omegat.gui.main.ProjectUICommands.projectReload()
    } as Runnable)
}