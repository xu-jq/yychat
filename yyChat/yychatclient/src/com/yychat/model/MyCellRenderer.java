package com.yychat.model;

import javax.swing.*;
import java.awt.*;

/**
 * @author changing
 * @create 2022-05-31 10:46
 */
public class MyCellRenderer extends JLabel implements ListCellRenderer {
    private static final long serialVersionUID = 3460394416991636990L;

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        User user = (User)value;
        String name = user.getUserName();
        setText(name);
        setIcon(new ImageIcon("yyChat/yychatclient/src/images/duck2.gif"));
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}
