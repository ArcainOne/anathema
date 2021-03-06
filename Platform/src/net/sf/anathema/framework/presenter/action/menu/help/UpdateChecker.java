package net.sf.anathema.framework.presenter.action.menu.help;

import net.sf.anathema.lib.control.IChangeListener;
import net.sf.anathema.lib.message.MessageType;
import net.sf.anathema.lib.resources.IResources;
import org.jmock.example.announcer.Announcer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class UpdateChecker implements IUpdateChecker {

  public static final String Update_Url = "http://anathema.github.com/version/version.txt";
  private final IResources resources;
  private final Announcer<IChangeListener> control = Announcer.to(IChangeListener.class);
  private IMessageData data;
  private String remoteVersion;
  private Boolean success = null;

  public UpdateChecker(IResources resources) {
    this.resources = resources;
  }

  public void checkForUpdates() {
    try {
      String response = getVersionData();
      String[] split = response.split("#"); //$NON-NLS-1$
      boolean newVersionAvailable = remoteIsNewer(split[1]);
      this.remoteVersion = split[0];
      if (newVersionAvailable) {
        this.success = true;
        this.data = new MessageData("Help.UpdateCheck.Outdated", MessageType.INFORMATION); //$NON-NLS-1$
      }
      else {
        this.success = true;
        this.data = new MessageData("Help.UpdateCheck.UpToDate", MessageType.INFORMATION); //$NON-NLS-1$
      }
    }
    catch (IOException e) {
      this.success = false;
      this.data = new MessageData("Help.UpdateCheck.IOException", MessageType.ERROR); //$NON-NLS-1$
    }
    catch (Exception e) {
      this.success = false;
      this.data = new MessageData("Help.UpdateCheck.GeneralException", MessageType.ERROR); //$NON-NLS-1$      
    }
    control.announce().changeOccurred();
  }

  @Override
  public Boolean isCheckSuccessful() {
    return success;
  }

  @Override
  public String getCurrentVersion() {
    return resources.getString("Anathema.Version.Numeric"); //$NON-NLS-1$
  }

  @Override
  public String getLatestVersion() {
    return remoteVersion;
  }

  @Override
  public IMessageData getMessageData() {
    return data;
  }

  private boolean remoteIsNewer(String string) throws ParseException {
    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ENGLISH);
    Date currentVersionDate = dateFormat.parse(resources.getString("Anathema.Version.Built")); //$NON-NLS-1$
    Date remoteVersionDate = dateFormat.parse(string);
    return currentVersionDate.compareTo(remoteVersionDate) < 0;
  }

  private String getVersionData() throws IOException {
    URL url = new URL(Update_Url); //$NON-NLS-1$
    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
    String response = in.readLine();
    in.close();
    return response;
  }

  @Override
  public void addDataChangedListener(IChangeListener listener) {
    control.addListener(listener);
  }
}