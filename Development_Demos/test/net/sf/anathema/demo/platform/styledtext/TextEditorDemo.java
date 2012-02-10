package net.sf.anathema.demo.platform.styledtext;

import de.jdemo.extensions.SwingDemoCase;
import de.jdemo.junit.DemoAsTestRunner;
import net.disy.commons.core.text.font.FontStyle;
import net.disy.commons.swing.action.SmartAction;
import net.sf.anathema.framework.styledtext.TextEditor;
import net.sf.anathema.framework.styledtext.model.ITextPart;
import net.sf.anathema.framework.styledtext.presentation.StyledTextManager;
import net.sf.anathema.framework.styledtext.presentation.TextFormat;
import net.sf.anathema.framework.styledtext.presentation.TextPart;
import org.junit.runner.RunWith;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import java.awt.*;

@RunWith(DemoAsTestRunner.class)
public class TextEditorDemo extends SwingDemoCase {

  public void demoEmptyTextEditor() {
    show(createEditor(new DefaultStyledDocument()).getComponent());
  }

  private TextEditor createEditor(StyledDocument document) {
    return new TextEditor(document, new DemoTextEditorProperties(), new Dimension(200, 200));
  }

  public void demoFilledTextEditor() {
    ITextPart[] textParts = new ITextPart[]{
      new TextPart("Dieser Text ist fett.", new TextFormat(FontStyle.BOLD, false)), //$NON-NLS-1$
      new TextPart(" Gefolgt von einem kursiven Teil, ", new TextFormat(FontStyle.ITALIC, false)), //$NON-NLS-1$
      new TextPart("und einmal beides.\n", new TextFormat(FontStyle.BOLD_ITALIC, false)), //$NON-NLS-1$
      new TextPart("Doch wir koennen auch schlicht und einfach..\n", new TextFormat(FontStyle.PLAIN, false)), //$NON-NLS-1$
      new TextPart("Und unterstrichen fuer die besonderen Gelegenheiten..\n", new TextFormat(FontStyle.PLAIN, true)),}; //$NON-NLS-1$
    DefaultStyledDocument document = new DefaultStyledDocument();
    final StyledTextManager textConverter = new StyledTextManager(document);
    textConverter.setText(textParts);
    TextEditor textEditor = createEditor(document);
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(textEditor.getComponent(), BorderLayout.CENTER);
    panel.add(new JButton(createTextPartOutputAction(textConverter)), BorderLayout.SOUTH);
    show(panel);
  }

  private SmartAction createTextPartOutputAction(final StyledTextManager textConverter) {
    return new SmartAction("Print Text Formats") { //$NON-NLS-1$
      private static final long serialVersionUID = -1186129067427433694L;

      @Override
      protected void execute(Component parentComponent) {
        ITextPart[] parts = textConverter.getTextParts();
        for (ITextPart part : parts) {
          System.out.println(part);
        }
      }
    };
  }
}