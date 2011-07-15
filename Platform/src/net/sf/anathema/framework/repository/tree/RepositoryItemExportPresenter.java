package net.sf.anathema.framework.repository.tree;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.disy.commons.core.message.Message;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.sf.anathema.framework.presenter.resources.FileUi;
import net.sf.anathema.framework.repository.access.IRepositoryFileAccess;
import net.sf.anathema.framework.view.PrintNameFile;
import net.sf.anathema.lib.control.change.IChangeListener;
import net.sf.anathema.lib.gui.IPresenter;
import net.sf.anathema.lib.gui.file.FileChoosingUtilities;
import net.sf.anathema.lib.logging.Logger;
import net.sf.anathema.lib.resources.IResources;

public class RepositoryItemExportPresenter implements IPresenter {

  private final IResources resources;
  private final IRepositoryTreeModel model;
  private final RepositoryTreeView view;
  private final RepositoryZipPathCreator creator;
  private final AmountMessaging messaging;

  public RepositoryItemExportPresenter(
      IResources resources,
      RepositoryTreeModel repositoryTreeModel,
      RepositoryTreeView treeView,
      AmountMessaging fileCountMessaging) {
    this.resources = resources;
    this.model = repositoryTreeModel;
    this.view = treeView;
    this.messaging = fileCountMessaging;
    this.creator = new RepositoryZipPathCreator(model.getRepositoryPath());
  }

  public void initPresentation() {
    final SmartAction action = new SmartAction(resources.getString("AnathemaCore.Tools.RepositoryView.ExportName"), //$NON-NLS-1$
        new FileUi(resources).getExportFileIcon()) {
      private static final long serialVersionUID = 134179579435852345L;

      @Override
      protected void execute(Component parentComponent) {
        try {
          File saveFile = FileChoosingUtilities.selectSaveFile(parentComponent, "Export.zip"); //$NON-NLS-1$
          if (saveFile == null) {
            return;
          }
          ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(saveFile));
          zipOutputStream.setComment(resources.getString("Anathema.Version.Numeric")); //$NON-NLS-1$
          PrintNameFile[] printNameFiles = model.getPrintNameFilesInSelection();
          for (PrintNameFile printNameFile : printNameFiles) {
            IRepositoryFileAccess access = model.getFileAccess(printNameFile);
            for (File file : access.getFiles()) {
              ZipEntry entry = createZipEntry(file, printNameFile);
              zipOutputStream.putNextEntry(entry);
              StreamUtilities.writeInputStreamToOutputStream(access.openInputStream(file), zipOutputStream);
              zipOutputStream.closeEntry();
            }
          }
          zipOutputStream.close();
          messaging.addMessage("AnathemaCore.Tools.RepositoryView.ExportDoneMessage", printNameFiles.length); //$NON-NLS-1$
        }
        catch (IOException e) {
          MessageDialogFactory.showMessageDialog(parentComponent, new Message(
              resources.getString("AnathemaCore.Tools.RepositoryView.FileError"), //$NON-NLS-1$
              e));
          Logger.getLogger(getClass()).error(e);
        }
      }

    };
    action.setToolTipText(resources.getString("AnathemaCore.Tools.RepositoryView.ExportToolTip")); //$NON-NLS-1$
    view.addActionButton(action);
    model.addTreeSelectionChangeListener(new IChangeListener() {
      public void changeOccured() {
        action.setEnabled(model.canSelectionBeDeleted());
      }
    });
    action.setEnabled(false);
  }

  private ZipEntry createZipEntry(File file, PrintNameFile printNameFile) {
    ZipEntry entry = new ZipEntry(creator.createZipPath(file));
    entry.setComment(resources.getString("Anathema.Version.Numeric") + "#" + printNameFile.getItemType() + "#" + printNameFile.getRepositoryId()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    return entry;
  }
}