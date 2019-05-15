package com.github.blombler008.twitchbot.window.panels.config;/*
 *
 * MIT License
 *
 * Copyright (c) 2019 blombler008
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import com.github.blombler008.twitchbot.window.GUIGraphicsWindow;
import com.github.blombler008.twitchbot.window.panels.ConfigPanel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.WindowConstants.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class TabWindow {


    private String labelPositionText = "Position:";
    private String labelTitleText = "Title:";
    private String labelSizeText = "Size:";
    private String labelDefaultExitMethodText = "Default Exit Method:";

    private String buttonChangeText = "Change";
    private String buttonResetText = "Reset";
    private String buttonCenterText = "Center";
    private String X = "X";

    private JButton resetButtonPosition;
    private JButton centerButtonPosition;
    private JButton changeButtonPosition;

    private JButton changeButtonTitle;
    private JButton resetButtonTitle;

    private JButton resetButtonSize;
    private JButton changeButtonSize;

    private JButton changeButtonDefaultExitMethod;
    private JButton resetButtonDefaultExitMethod;

    private JLabel labelSize;
    private JLabel labelDefaultExitMethod;
    private JLabel labelTimesX;
    private JLabel labelTimesX2;
    private JLabel labelTitle;
    private JLabel labelPosition;
    private JComboBox<ConfigPanel.Entry> comboBoxDefaultExitMethod;
    private JTextField positionTextFieldX;
    private JTextField positionTextFieldY;
    private JTextField titleTextField;
    private JTextField sizeTextFieldX;
    private JTextField sizeTextFieldY;
    private JPanel panel;
    private GUIGraphicsWindow frame;

    public TabWindow(GUIGraphicsWindow frame) {
        this.frame = frame;

        panel = new JPanel();

        labelPosition = new JLabel(labelPositionText);
        labelTitle = new JLabel(labelTitleText);
        labelSize = new JLabel(labelSizeText);
        labelDefaultExitMethod = new JLabel(labelDefaultExitMethodText);

        changeButtonPosition = new JButton(buttonChangeText);
        centerButtonPosition = new JButton(buttonCenterText);
        resetButtonPosition = new JButton(buttonResetText);

        changeButtonTitle = new JButton(buttonChangeText);
        resetButtonTitle = new JButton(buttonResetText);

        changeButtonSize = new JButton(buttonChangeText);
        resetButtonSize = new JButton(buttonResetText);

        changeButtonDefaultExitMethod = new JButton(buttonChangeText);
        resetButtonDefaultExitMethod = new JButton(buttonResetText);

        labelTimesX = new JLabel(X);
        labelTimesX2 = new JLabel(X);

        comboBoxDefaultExitMethod = new JComboBox<>();

        positionTextFieldX = new JTextField();
        positionTextFieldY = new JTextField();
        titleTextField = new JTextField();
        sizeTextFieldX = new JTextField();
        sizeTextFieldY = new JTextField();

        comboBoxDefaultExitMethod.insertItemAt(new ConfigPanel.Entry("DO_NOTHING_ON_CLOSE", DO_NOTHING_ON_CLOSE), DO_NOTHING_ON_CLOSE);
        comboBoxDefaultExitMethod.insertItemAt(new ConfigPanel.Entry("HIDE_ON_CLOSE", HIDE_ON_CLOSE), HIDE_ON_CLOSE);
        comboBoxDefaultExitMethod.insertItemAt(new ConfigPanel.Entry("DISPOSE_ON_CLOSE", DISPOSE_ON_CLOSE), DISPOSE_ON_CLOSE);
        comboBoxDefaultExitMethod.insertItemAt(new ConfigPanel.Entry("EXIT_ON_CLOSE", EXIT_ON_CLOSE), EXIT_ON_CLOSE);

        positionTextFieldX.setColumns(10);
        positionTextFieldY.setColumns(10);

        titleTextField.setColumns(10);

        sizeTextFieldX.setColumns(10);
        sizeTextFieldY.setColumns(10);

        labelPosition.setHorizontalAlignment(SwingConstants.RIGHT);
        labelTitle.setHorizontalAlignment(SwingConstants.RIGHT);
        labelSize.setHorizontalAlignment(SwingConstants.RIGHT);
        labelDefaultExitMethod.setHorizontalAlignment(SwingConstants.RIGHT);
        labelTimesX.setHorizontalAlignment(SwingConstants.CENTER);
        labelTimesX2.setHorizontalAlignment(SwingConstants.CENTER);
        // Register button clicks //

        resetButtonSize.addMouseListener(new MouseAdapterHandler());
        changeButtonSize.addMouseListener(new MouseAdapterHandler());

        resetButtonPosition.addMouseListener(new MouseAdapterHandler());
        changeButtonPosition.addMouseListener(new MouseAdapterHandler());
        centerButtonPosition.addMouseListener(new MouseAdapterHandler());

        resetButtonDefaultExitMethod.addMouseListener(new MouseAdapterHandler());
        changeButtonDefaultExitMethod.addMouseListener(new MouseAdapterHandler());

        changeButtonTitle.addMouseListener(new MouseAdapterHandler());
        resetButtonTitle.addMouseListener(new MouseAdapterHandler());

        // Label Positions //
        labelPosition.setBounds(10, 77, 118, 22); // Position

        labelTitle.setBounds(10, 44, 118, 22); // Title

        labelSize.setBounds(10, 11, 118, 22); // Size

        labelDefaultExitMethod.setBounds(10, 110, 118, 22); // Default Exit Method

        labelTimesX.setBounds(256, 11, 26, 22); // X between Size width and height
        labelTimesX2.setBounds(256, 77, 26, 22); // X between Position X and Y

        // Text field Positions (and ComboBox) //
        positionTextFieldX.setBounds(138, 77, 118, 22); // Position X
        positionTextFieldY.setBounds(282, 77, 118, 22); // Position Y

        titleTextField.setBounds(138, 44, 262, 22); // Title

        sizeTextFieldX.setBounds(138, 11, 118, 22); // Size width
        sizeTextFieldY.setBounds(282, 11, 118, 22); // Size height

        comboBoxDefaultExitMethod.setBounds(138, 111, 262, 20); // Default Exit Method (ComboBox)

        // Button Positions //
        changeButtonPosition.setBounds(414, 77, 90, 22); // Position Change
        resetButtonPosition.setBounds(518, 77, 90, 22); // Position reset
        centerButtonPosition.setBounds(622, 77, 90, 22); // Position Center

        changeButtonTitle.setBounds(414, 44, 90, 22); // Title
        resetButtonTitle.setBounds(518, 44, 90, 22); // Title

        changeButtonSize.setBounds(414, 11, 90, 22); // Size Change
        resetButtonSize.setBounds(518, 11, 90, 22); // Size Reset

        changeButtonDefaultExitMethod.setBounds(414, 110, 90, 22); // Default Exit Method Change
        resetButtonDefaultExitMethod.setBounds(518, 110, 90, 22); // Default Exit Method Reset
        // Adding components to panel //
        panel.add(labelSize);
        panel.add(labelTitle);
        panel.add(labelPosition);
        panel.add(labelDefaultExitMethod);

        panel.add(labelTimesX);
        panel.add(labelTimesX2);

        panel.add(sizeTextFieldX);
        panel.add(sizeTextFieldY);

        panel.add(titleTextField);

        panel.add(comboBoxDefaultExitMethod);

        panel.add(positionTextFieldX);
        panel.add(positionTextFieldY);

        panel.add(changeButtonSize);
        panel.add(resetButtonSize);

        panel.add(centerButtonPosition);
        panel.add(resetButtonPosition);
        panel.add(changeButtonPosition);

        panel.add(changeButtonTitle);
        panel.add(resetButtonTitle);

        panel.add(changeButtonDefaultExitMethod);
        panel.add(resetButtonDefaultExitMethod);

        panel.setLayout(null);
        updateAll();
    }

    public void updateAll() {
        updateComboBox();
        updateSize(frame.getSize());
        updatePosition(frame.getLocation());
        updateTitle();
    }

    public void resetAll() {
        resetSize();
        resetTitle();
        resetPosition();
        resetComboBox();

        updateComboBox();
        updateSize(frame.getSize());
        updatePosition(frame.getLocation());
        updateTitle();
    }

    public void resetTitle() {
        titleTextField.setText(frame.getDefaultTitle());
    }

    public void resetPosition() {
        frame.setLocation(frame.getDefaultLocation());
    }

    public void resetSize() {
        frame.setSize(frame.getDefaultSize());
    }

    public void resetComboBox() {
        frame.setDefaultCloseOperation(frame.getDefaultExitOperation());
    }


    public void updateTitle() {
        String title = frame.getTitle();

        titleTextField.setText(title);
    }

    public void updatePosition(Point dim) {
        int X = dim.x;
        int Y = dim.y;

        positionTextFieldX.setText(Integer.toString(X));
        positionTextFieldY.setText(Integer.toString(Y));
    }

    public void updateSize(Dimension size) {
        int X = size.width;
        int Y = size.height;

        sizeTextFieldX.setText(Integer.toString(X));
        sizeTextFieldY.setText(Integer.toString(Y));
    }

    public void updateComboBox() {
        comboBoxDefaultExitMethod.setSelectedIndex(frame.getDefaultCloseOperation());
    }

    public JPanel get() {
        return panel;
    }

    private class MouseAdapterHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {



            // Size //
            if(e.getSource().equals(resetButtonSize)) {
                resetSize();
                updateSize(frame.getSize());
            }

            if(e.getSource().equals(changeButtonSize)) {
                Dimension dim;
                try {
                    int x = Integer.parseInt(sizeTextFieldX.getText());
                    int y = Integer.parseInt(sizeTextFieldY.getText());
                    dim = new Dimension(x,y);
                    frame.setSize(dim);
                    updateSize(frame.getSize());
                } catch (Exception ignore) {}
            }








            // Position //
            if(e.getSource().equals(resetButtonPosition)) {
                resetPosition();
            }
            if(e.getSource().equals(centerButtonPosition)) {
                frame.center();
                updatePosition(frame.getLocation());
            }

            if(e.getSource().equals(changeButtonPosition)) {
                Point dim;
                try {
                    int x = Integer.parseInt(positionTextFieldX.getText());
                    int y = Integer.parseInt(positionTextFieldY.getText());
                    dim = new Point(x,y);
                    frame.setLocation(dim);
                    updatePosition(frame.getLocation());
                } catch (Exception ignore) {}
            }








            // Combo Box //
            if(e.getSource().equals(changeButtonDefaultExitMethod)) {
                frame.setDefaultCloseOperation(comboBoxDefaultExitMethod.getSelectedIndex());
                updateComboBox();
            }
            if(e.getSource().equals(resetButtonDefaultExitMethod)) {
                resetComboBox();
                updateComboBox();
            }









            // Title //
            if(e.getSource().equals(resetButtonTitle)) {
                frame.setTitle(frame.getDefaultTitle());
                updateTitle();
            }
            if(e.getSource().equals(changeButtonTitle)) {
                frame.setTitle(titleTextField.getText());
                updateTitle();
            }
        }


    }
}
