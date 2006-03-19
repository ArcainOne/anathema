package net.sf.anathema.lib.workflow.labelledvalue.view;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogPanel;
import net.disy.commons.swing.layout.grid.IDialogComponent;
import net.sf.anathema.lib.gui.layout.AnathemaLayoutUtilities;
import net.sf.anathema.lib.workflow.labelledvalue.ILabelledAlotmentView;

public class LabelledAlotmentView extends AbstractLabelledIntegerValueView implements ILabelledAlotmentView {

  private final JLabel seperatorLabel;
  private final JLabel maxPointLabel;

  public LabelledAlotmentView(String labelText, int currentPoints, int maxPoints) {
    this(labelText, currentPoints, maxPoints, 2);
  }

  public LabelledAlotmentView(String labelText, int currentPoints, int maxPoints, int maxValueLength) {
    super(labelText, createLengthString(maxValueLength), currentPoints, true);
    this.maxPointLabel = createLabel(
        String.valueOf(maxPoints),
        createLengthString(maxValueLength),
        SwingConstants.RIGHT,
        true);
    this.seperatorLabel = createLabel("/", "/", SwingConstants.CENTER, true); //$NON-NLS-1$//$NON-NLS-2$
  }

  @Override
  public void addComponents(GridDialogPanel dialogPanel) {
    dialogPanel.add(new IDialogComponent() {
      public int getColumnCount() {
        return 4;
      }

      public void fillInto(JPanel panel, int columnCount) {
        panel.add(titleLabel, GridDialogLayoutData.FILL_HORIZONTAL);
        panel.add(valueLabel, GridDialogLayoutData.FILL_HORIZONTAL);
        panel.add(seperatorLabel, AnathemaLayoutUtilities.createAlignedGridDialogData(GridAlignment.END));
        panel.add(maxPointLabel, AnathemaLayoutUtilities.createAlignedGridDialogData(GridAlignment.END));
      }
    });
  }

  public void setTextColor(Color color) {
    titleLabel.setForeground(color);
    valueLabel.setForeground(color);
    seperatorLabel.setForeground(color);
    maxPointLabel.setForeground(color);
  }

  public void setFontStyle(int style) {
    titleLabel.setFont(titleLabel.getFont().deriveFont(style));
    valueLabel.setFont(valueLabel.getFont().deriveFont(style));
    seperatorLabel.setFont(seperatorLabel.getFont().deriveFont(style));
    maxPointLabel.setFont(maxPointLabel.getFont().deriveFont(style));
  }

  public void setAlotment(int value) {
    maxPointLabel.setText(String.valueOf(value));
  }
}