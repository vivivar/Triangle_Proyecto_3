/*
 * IDE-Triangle v1.0
 * Main.java
 */

package GUI;
import Core.Console.InputRedirector;
import Core.Console.OutputRedirector;
import Core.IDE.IDEDisassembler;
import Core.IDE.IDEInterpreter;
import Core.Visitors.TableVisitor;
// import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import Triangle.IDECompiler;
import Core.ExampleFileFilter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import Core.Visitors.TreeVisitor;
import javax.swing.tree.DefaultMutableTreeNode;
import Triangle.SyntacticAnalyzer.*;
import Triangle.AbstractSyntaxTrees.*;
import Triangle.SyntacticAnalyzer.*;
import Triangle.AbstractSyntaxTrees.*;
import Triangle.CodeGenerator.LLVMGenerator;
import Triangle.Compiler;
import Triangle.ErrorReporter;
import java.io.IOException;
import java.io.InputStreamReader;





/**
 * The Main class. Contains the main form.
 *
 * @author Luis Leopoldo P�rez <luiperpe@ns.isi.ulatina.ac.cr>
 */
public class Main extends javax.swing.JFrame {

    // <editor-fold defaultstate="collapsed" desc=" Methods ">
    
    /**
     * Creates new form Main.
     */
    public Main() {        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());            
        } catch (Exception e) { }
        
        initComponents();
        setSize(640, 480);
        setVisible(true);
        directory = new File(".");
    }
    
    /**
     * Checks if the file has been changed - used to enable/disable 
     * the "Save" button.
     */
    private void checkSaveChanges() {
        if (((FileFrame)desktopPane.getSelectedFrame()).hasChanged()) {
            buttonSave.setEnabled(true);
            saveMenuItem.setEnabled(true);
        }
        else {
            buttonSave.setEnabled(false);
            saveMenuItem.setEnabled(false);
        }
    }
    
    /**
     * Checks if there are any open documents. Disables some buttons and
     * menu options if no document is open. 
     */
    private void checkPaneChanges() {
        if (desktopPane.getComponentCount() == 0) {
            saveAsMenuItem.setEnabled(false);
            saveMenuItem.setEnabled(false);
            buttonSave.setEnabled(false);            
            buttonCut.setEnabled(false);
            buttonCopy.setEnabled(false);
            buttonPaste.setEnabled(false);            
            buttonCompile.setEnabled(false);
            buttonRun.setEnabled(false);
            cutMenuItem.setEnabled(false);
            copyMenuItem.setEnabled(false);
            pasteMenuItem.setEnabled(false);            
            compileMenuItem.setEnabled(false);
            runMenuItem.setEnabled(false);
            LLVM.setEnabled(false);   
        } else
            checkSaveChanges();
    }
    
    /**
     * Creates a new document frame and inserts it into the desktop pane,
     * enabling its buttons and menu options.
     *
     * @param title Title of the frame.
     * @param contents File contents.
     * @return The new file frame.
     */
    private FileFrame addInternalFrame(String title, String contents) {
        FileFrame x = new FileFrame(delegateSaveButton, delegateMouse, delegateInternalFrame, delegateEnter);
        
        x.setTitle(title);
        x.setSize(540, 250);
        x.setSourcePaneText(contents);
        x.setPreviousSize(contents.length());
        x.setPreviousText(contents);
        desktopPane.add(x);        
        x.setVisible(true);
        
        saveAsMenuItem.setEnabled(true);        
        buttonCut.setEnabled(true);
        buttonCopy.setEnabled(true);
        buttonPaste.setEnabled(true);            
        cutMenuItem.setEnabled(true);
        copyMenuItem.setEnabled(true);
        pasteMenuItem.setEnabled(true);
        compileMenuItem.setEnabled(true);
        buttonCompile.setEnabled(true);
        LLVM.setEnabled(true);
       
        checkSaveChanges();        
        return(x);
    }
    
    /**
     * Opens a file chooser dialog, used in the "Open" and "Save" options.
     * @return The JFileChooser dialog object.
     */
    private JFileChooser drawFileChooser() {
        JFileChooser chooser = new JFileChooser();
        ExampleFileFilter filter = new ExampleFileFilter();        
        filter.setDescription("Triangle files");
        filter.addExtension("tri");
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(directory);
        
        return(chooser);
    }

    /**
     * Main method, instantiates the Main class.
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolBarsPanel = new javax.swing.JPanel();
        fileToolBar = new javax.swing.JToolBar();
        buttonNew = new javax.swing.JButton();
        buttonOpen = new javax.swing.JButton();
        buttonSave = new javax.swing.JButton();
        editToolBar = new javax.swing.JToolBar();
        buttonCut = new javax.swing.JButton();
        buttonCopy = new javax.swing.JButton();
        buttonPaste = new javax.swing.JButton();
        triangleToolBar = new javax.swing.JToolBar();
        buttonCompile = new javax.swing.JButton();
        buttonRun = new javax.swing.JButton();
        LLVM = new javax.swing.JButton();
        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        separatorExit = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        triangleMenu = new javax.swing.JMenu();
        compileMenuItem = new javax.swing.JMenuItem();
        runMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("IDE-Triangle 1.1");
        setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        setIconImage(new ImageIcon(this.getClass().getResource("Icons/iconMain.gif")).getImage());
        setLocationByPlatform(true);
        setName("mainFrame"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        toolBarsPanel.setFocusable(false);
        toolBarsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        fileToolBar.setFocusable(false);
        fileToolBar.setName("File"); // NOI18N
        fileToolBar.setRequestFocusEnabled(false);

        buttonNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconFileNew.gif"))); // NOI18N
        buttonNew.setToolTipText("New...");
        buttonNew.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        buttonNew.setBorderPainted(false);
        buttonNew.setFocusPainted(false);
        buttonNew.setFocusable(false);
        buttonNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileToolBar.add(buttonNew);

        buttonOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconFileOpen.gif"))); // NOI18N
        buttonOpen.setToolTipText("Open...");
        buttonOpen.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        buttonOpen.setBorderPainted(false);
        buttonOpen.setFocusPainted(false);
        buttonOpen.setFocusable(false);
        buttonOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileToolBar.add(buttonOpen);

        buttonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconFileSave.gif"))); // NOI18N
        buttonSave.setToolTipText("Save...");
        buttonSave.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        buttonSave.setBorderPainted(false);
        buttonSave.setEnabled(false);
        buttonSave.setFocusPainted(false);
        buttonSave.setFocusable(false);
        buttonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileToolBar.add(buttonSave);

        toolBarsPanel.add(fileToolBar);

        editToolBar.setFocusable(false);
        editToolBar.setName("Edit"); // NOI18N
        editToolBar.setRequestFocusEnabled(false);

        buttonCut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconEditCut.gif"))); // NOI18N
        buttonCut.setToolTipText("Cut...");
        buttonCut.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        buttonCut.setBorderPainted(false);
        buttonCut.setEnabled(false);
        buttonCut.setFocusPainted(false);
        buttonCut.setFocusable(false);
        buttonCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cutMenuItemActionPerformed(evt);
            }
        });
        editToolBar.add(buttonCut);

        buttonCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconEditCopy.gif"))); // NOI18N
        buttonCopy.setToolTipText("Copy...");
        buttonCopy.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        buttonCopy.setBorderPainted(false);
        buttonCopy.setEnabled(false);
        buttonCopy.setFocusPainted(false);
        buttonCopy.setFocusable(false);
        buttonCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyMenuItemActionPerformed(evt);
            }
        });
        editToolBar.add(buttonCopy);

        buttonPaste.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconEditPaste.gif"))); // NOI18N
        buttonPaste.setToolTipText("Paste...");
        buttonPaste.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        buttonPaste.setBorderPainted(false);
        buttonPaste.setEnabled(false);
        buttonPaste.setFocusPainted(false);
        buttonPaste.setFocusable(false);
        buttonPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteMenuItemActionPerformed(evt);
            }
        });
        editToolBar.add(buttonPaste);

        toolBarsPanel.add(editToolBar);

        triangleToolBar.setFocusable(false);
        triangleToolBar.setName("Triangle"); // NOI18N
        triangleToolBar.setRequestFocusEnabled(false);

        buttonCompile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconTriangleCompile.gif"))); // NOI18N
        buttonCompile.setToolTipText("Compile...");
        buttonCompile.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        buttonCompile.setBorderPainted(false);
        buttonCompile.setEnabled(false);
        buttonCompile.setFocusPainted(false);
        buttonCompile.setFocusable(false);
        buttonCompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileMenuItemActionPerformed(evt);
            }
        });
        triangleToolBar.add(buttonCompile);

        buttonRun.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconTriangleRun.gif"))); // NOI18N
        buttonRun.setToolTipText("Run...");
        buttonRun.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        buttonRun.setBorderPainted(false);
        buttonRun.setEnabled(false);
        buttonRun.setFocusPainted(false);
        buttonRun.setFocusable(false);
        buttonRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runMenuItemActionPerformed(evt);
            }
        });
        triangleToolBar.add(buttonRun);

        toolBarsPanel.add(triangleToolBar);

        LLVM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/llvm-logo.jpg"))); // NOI18N
        LLVM.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        LLVM.setEnabled(false);
        LLVM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LLVMActionPerformed(evt);
            }
        });
        toolBarsPanel.add(LLVM);

        getContentPane().add(toolBarsPanel, java.awt.BorderLayout.NORTH);

        desktopPane.setBackground(new java.awt.Color(0, 103, 201));
        desktopPane.setAutoscrolls(true);
        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

        menuBar.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N

        fileMenu.setMnemonic('F');
        fileMenu.setText("File");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        newMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconFileNew.gif"))); // NOI18N
        newMenuItem.setMnemonic('N');
        newMenuItem.setText("New");
        newMenuItem.setRequestFocusEnabled(false);
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        openMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconFileOpen.gif"))); // NOI18N
        openMenuItem.setMnemonic('O');
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconFileSave.gif"))); // NOI18N
        saveMenuItem.setMnemonic('S');
        saveMenuItem.setText("Save");
        saveMenuItem.setEnabled(false);
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setMnemonic('A');
        saveAsMenuItem.setText("Save As...");
        saveAsMenuItem.setEnabled(false);
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(separatorExit);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setMnemonic('E');
        editMenu.setText("Edit");

        cutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        cutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconEditCut.gif"))); // NOI18N
        cutMenuItem.setMnemonic('t');
        cutMenuItem.setText("Cut");
        cutMenuItem.setEnabled(false);
        cutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cutMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(cutMenuItem);

        copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        copyMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconEditCopy.gif"))); // NOI18N
        copyMenuItem.setMnemonic('C');
        copyMenuItem.setText("Copy");
        copyMenuItem.setEnabled(false);
        copyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(copyMenuItem);

        pasteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        pasteMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconEditPaste.gif"))); // NOI18N
        pasteMenuItem.setMnemonic('P');
        pasteMenuItem.setText("Paste");
        pasteMenuItem.setToolTipText("");
        pasteMenuItem.setEnabled(false);
        pasteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pasteMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(pasteMenuItem);

        menuBar.add(editMenu);

        triangleMenu.setMnemonic('T');
        triangleMenu.setText("Triangle");

        compileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        compileMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconTriangleCompile.gif"))); // NOI18N
        compileMenuItem.setMnemonic('C');
        compileMenuItem.setText("Compile");
        compileMenuItem.setEnabled(false);
        compileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileMenuItemActionPerformed(evt);
            }
        });
        triangleMenu.add(compileMenuItem);

        runMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        runMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconTriangleRun.gif"))); // NOI18N
        runMenuItem.setMnemonic('R');
        runMenuItem.setText("Run");
        runMenuItem.setEnabled(false);
        runMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runMenuItemActionPerformed(evt);
            }
        });
        triangleMenu.add(runMenuItem);

        menuBar.add(triangleMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");

        aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/Icons/iconHelpAbout.gif"))); // NOI18N
        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc=" Event Handlers Implementation ">
    
    /**
     * Handles the "Run TAM Program" button and menu option.
     */
    private void runMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runMenuItemActionPerformed
        ((FileFrame)desktopPane.getSelectedFrame()).clearConsole();
        ((FileFrame)desktopPane.getSelectedFrame()).selectConsole();
        output.setDelegate(delegateConsole);
        runMenuItem.setEnabled(false);
        buttonRun.setEnabled(false);
        compileMenuItem.setEnabled(false);
        buttonCompile.setEnabled(false);
        interpreter.Run(desktopPane.getSelectedFrame().getTitle().replace(".tri", ".tam"));
    }//GEN-LAST:event_runMenuItemActionPerformed

    /** 
     * Handles the "Close" program option
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        while (desktopPane.getComponentCount() > 0) {
            try { 
                desktopPane.getSelectedFrame().setClosed(true);
            } catch (Exception e) { }
        }
    }//GEN-LAST:event_formWindowClosing

    /** 
     * Handles the "Paste Text" button and menu option.
     */
    private void pasteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteMenuItemActionPerformed
        ((FileFrame)desktopPane.getSelectedFrame()).pasteText(Clip.getClipboardContents());
    }//GEN-LAST:event_pasteMenuItemActionPerformed

    /**
     * Handles the "Cut Text" button and menu option.
     */
    private void cutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cutMenuItemActionPerformed
        Clip.setClipboardContents(((FileFrame)desktopPane.getSelectedFrame()).getSelectedText());
        ((FileFrame)desktopPane.getSelectedFrame()).cutText();
    }//GEN-LAST:event_cutMenuItemActionPerformed

    /**
     * Handles the "Copy Text" button and menu option.
     */
    private void copyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyMenuItemActionPerformed
        Clip.setClipboardContents(((FileFrame)desktopPane.getSelectedFrame()).getSelectedText());
    }//GEN-LAST:event_copyMenuItemActionPerformed

    /** 
     * Handles the "Save As" button and menu option.
     */
    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        boolean _previouslySaved = ((FileFrame)desktopPane.getSelectedFrame()).getPreviouslySaved();        
        ((FileFrame)desktopPane.getSelectedFrame()).setPreviouslySaved(false);
        saveMenuItemActionPerformed(evt);
        ((FileFrame)desktopPane.getSelectedFrame()).setPreviouslySaved(_previouslySaved);        
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    /**
     * Handles the "About" menu option.
     */
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        new AboutDialog(this, true).setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    /**
     * Handles the "Open File" button and menu option.
     */
    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        JFileChooser chooser = drawFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                directory = chooser.getCurrentDirectory();
                BufferedReader br = new BufferedReader(new FileReader(chooser.getSelectedFile()));
                String sr = "";
                while (br.ready())
                    sr += (char)br.read();
                br.close();
                addInternalFrame(chooser.getSelectedFile().getPath(), sr.replace("\r\n", "\n")).setPreviouslySaved(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An error occurred while trying to open the specified file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_openMenuItemActionPerformed

    /**
     * Handles the "Compile" button and menu option.
     */
    private void compileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compileMenuItemActionPerformed
        if ((!((FileFrame)desktopPane.getSelectedFrame()).getPreviouslySaved()) || ((FileFrame)desktopPane.getSelectedFrame()).hasChanged()) {
            saveMenuItemActionPerformed(null);
        }
        
        if (((FileFrame)desktopPane.getSelectedFrame()).getPreviouslySaved()) {
            ((FileFrame)desktopPane.getSelectedFrame()).selectConsole();
            ((FileFrame)desktopPane.getSelectedFrame()).clearConsole();
            ((FileFrame)desktopPane.getSelectedFrame()).clearTAMCode();
            ((FileFrame)desktopPane.getSelectedFrame()).clearTree();
            ((FileFrame)desktopPane.getSelectedFrame()).clearTable();
            new File(desktopPane.getSelectedFrame().getTitle().replace(".tri", ".tam")).delete();
            
            output.setDelegate(delegateConsole);            
            if (compiler.compileProgram(desktopPane.getSelectedFrame().getTitle())) {           
                output.setDelegate(delegateTAMCode);
                disassembler.Disassemble(desktopPane.getSelectedFrame().getTitle().replace(".tri", ".tam"));
                ((FileFrame)desktopPane.getSelectedFrame()).setTree((DefaultMutableTreeNode)treeVisitor.visitProgram(compiler.getAST(), null));
                ((FileFrame)desktopPane.getSelectedFrame()).setTable(tableVisitor.getTable(compiler.getAST()));
                
                runMenuItem.setEnabled(true);
                buttonRun.setEnabled(true);
            } else {
                ((FileFrame)desktopPane.getSelectedFrame()).highlightError(compiler.getErrorPosition());
                runMenuItem.setEnabled(false);
                buttonRun.setEnabled(false);
            }
        }
    }//GEN-LAST:event_compileMenuItemActionPerformed

    /**
     * Handles the "Save" button and menu option.
     */
    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        String fileName = ((FileFrame)desktopPane.getSelectedFrame()).getTitle();
        boolean overwrite = true;
        
        if (!(((FileFrame)desktopPane.getSelectedFrame()).getPreviouslySaved())) {
            JFileChooser chooser = drawFileChooser();
            
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                if (chooser.getSelectedFile().exists()) {
                    overwrite = (JOptionPane.showConfirmDialog(this, chooser.getSelectedFile().getName() + " already exists.\nWould you like to replace it?", "Overwrite?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
                }
                
                directory = chooser.getCurrentDirectory();
                
                fileName = chooser.getSelectedFile().getPath();
                if (!(fileName.contains(".tri")))
                    fileName += ".tri";
            } else
                overwrite = false;
        }
 
        if (overwrite) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                bw.write(((FileFrame)desktopPane.getSelectedFrame()).getSourcePaneText().replace("\n", "\r\n"));
                bw.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An error occurred while trying to save the specified file", "Error", JOptionPane.ERROR_MESSAGE);
            }

            ((FileFrame)desktopPane.getSelectedFrame()).setPreviouslySaved(true);
            ((FileFrame)desktopPane.getSelectedFrame()).setTitle(fileName);            
            ((FileFrame)desktopPane.getSelectedFrame()).setPreviouslyModified(false);
            ((FileFrame)desktopPane.getSelectedFrame()).setPreviousSize(((FileFrame)desktopPane.getSelectedFrame()).getSourcePaneText().length());
            ((FileFrame)desktopPane.getSelectedFrame()).setPreviousText(((FileFrame)desktopPane.getSelectedFrame()).getSourcePaneText());
                
            saveMenuItem.setEnabled(false);
            buttonSave.setEnabled(false);
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed

    /**
     * Handles the "New File" button and menu option.
     */
    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        addInternalFrame("Untitled-" + String.valueOf(untitledCount), "");      
        untitledCount++;
    }//GEN-LAST:event_newMenuItemActionPerformed

    /**
     * Handles the "Exit" menu option.
     */
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        formWindowClosing(null);
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void LLVMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LLVMActionPerformed
    FileFrame ff = (FileFrame) desktopPane.getSelectedFrame();
    if (ff == null) return;
    
    ff.clearConsole();
    ff.selectConsole();
    output.setDelegate(delegateConsole);

    String sourceCode = ff.getSourcePaneText();
    Program prog = Compiler.compileProgramFromSource(sourceCode);

    LLVMGenerator generator = new LLVMGenerator();
    prog.visit(generator, null);
    String llvmCode = generator.getOutput();

    ff.clearLLVMCode();
    ff.writeToLLVMCode(llvmCode);

    try {
        // Guardar el código LLVM a archivo
        FileWriter writer = new FileWriter("output.ll");
        writer.write(llvmCode);
        writer.close();
        System.out.println(">> Código LLVM guardado en output.ll");

        // Verificar que stdio-wrapper.c exista
        File stdioFile = new File("stdio-wrapper.c");
        if (!stdioFile.exists()) {
            System.err.println(">> Archivo stdio-wrapper.c no encontrado. Debe estar junto a output.ll");
            return;
        }
        try {
            ProcessBuilder pbKill = new ProcessBuilder("taskkill", "/F", "/IM", "output.exe");
            Process killProcess = pbKill.start();

            BufferedReader killReader = new BufferedReader(new InputStreamReader(killProcess.getInputStream()));
            String killLine;
            while ((killLine = killReader.readLine()) != null) {
                System.out.println("[TASKKILL] " + killLine);
            }
            killProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println(">> No se pudo cerrar instancias anteriores de output.exe (puede que no estuvieran abiertas).");
        }
        // Compilar .ll junto con stdio-wrapper.c
        ProcessBuilder pbCompile = new ProcessBuilder(
            "clang", "output.ll", "stdio-wrapper.c", "-o", "output.exe", "-llegacy_stdio_definitions"
        );
        pbCompile.redirectErrorStream(true); // Unir stdout + stderr
        Process compileProcess = pbCompile.start();

        BufferedReader compileReader = new BufferedReader(
            new InputStreamReader(compileProcess.getInputStream())
        );
        String compileLine;
        while ((compileLine = compileReader.readLine()) != null) {
            System.out.println("[CLANG] " + compileLine);
        }

        int compileExitCode = compileProcess.waitFor();
        if (compileExitCode == 0) {
            System.out.println(">> Compilación con clang completada correctamente.");
        } else {
            System.err.println(">> Error en la compilación con clang.");
            return;
        }

        // Ejecutar el ejecutable en nueva consola de Windows
        ProcessBuilder pbRun = new ProcessBuilder("cmd.exe", "/c", "start cmd /k output.exe");
        pbRun.start();
        System.out.println(">> Ejecutando el binario en nueva consola...");

    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
    }

    }//GEN-LAST:event_LLVMActionPerformed

    // </editor-fold>    
           
    // <editor-fold defaultstate="collapsed" desc=" Delegates and Listeners ">    
    /**
     * Runs every time a key is pressed in the text editor frame, thus
     * determining if the file contents have changed, activating the
     * "Save" button and menu option.
     */   
    KeyAdapter delegateSaveButton = new KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
             checkSaveChanges();
             ((FileFrame)desktopPane.getSelectedFrame()).UpdateRowColNumbers();
        }
    };
    
    MouseListener delegateMouse = new MouseListener() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            ((FileFrame)desktopPane.getSelectedFrame()).UpdateRowColNumbers();
        }
        
        public void mouseExited(java.awt.event.MouseEvent evt) {
        }

        public void mouseEntered(java.awt.event.MouseEvent evt) {
        }        
        
        public void mouseReleased(java.awt.event.MouseEvent evt) {
        }       

        public void mousePressed(java.awt.event.MouseEvent evt) {
        }        
    };
    
    /**
     * Several events for the MDI text editor frames. 
     */
    InternalFrameListener delegateInternalFrame = new InternalFrameListener() {        
        
        /**
         * Every time a frame is focused/activated, some buttons can be
         * enabled (e.g. "Save", "Cut", "Copy", "Paste").
         */
        public void internalFrameActivated(InternalFrameEvent evt) { 
            checkPaneChanges();
            ((FileFrame)desktopPane.getSelectedFrame()).UpdateRowColNumbers();
        }
        
        /**
         * Every time a frame is closed, some buttons can be
         * disabled (e.g. "Save", "Cut", "Copy", "Paste").
         */        
        public void internalFrameClosed(InternalFrameEvent evt) {
            checkPaneChanges();
        }
            
        /**
         * Before closing a frame, checks if the file has not been
         * saved yet.
         */
        public void internalFrameClosing(InternalFrameEvent evt) {
            if (((FileFrame)desktopPane.getSelectedFrame()).hasChanged()) { 
                if (JOptionPane.showConfirmDialog(null, "Do you want to save the changes to " + desktopPane.getSelectedFrame().getTitle() + "?", "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)    
                    saveMenuItemActionPerformed(null);    
            }
        }
        
        // Required by interface - not implemented
        public void internalFrameDeactivated(InternalFrameEvent evt) { }
        public void internalFrameDeiconified(InternalFrameEvent evt) { }
        public void internalFrameIconified(InternalFrameEvent evt) { }
        public void internalFrameOpened(InternalFrameEvent evt) { }
    };
    
    
    /**
     * Used to redirect the console output - writes in the "Console" panel.     
     */
    ActionListener delegateConsole = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            while (output.peekQueue())
                ((FileFrame)desktopPane.getSelectedFrame()).writeToConsole(output.readQueue());
        }
    };
    
    /**
     * Used to redirect the console output - writes in the "TAM Code" pane.
     */
    ActionListener delegateTAMCode = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            while (output.peekQueue())
                ((FileFrame)desktopPane.getSelectedFrame()).writeToTAMCode(output.readQueue());
        }        
    };
    
    /**
     * Used to redirect the console input - enables the "Console Input" text box.
     */
    ActionListener delegateInput = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            ((FileFrame)desktopPane.getSelectedFrame()).setInputEnabled(true);
        }
    };
    
    /**
     * Used to redirect the console input - writes the user input in the console.
     */
    ActionListener delegateEnter = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            ((FileFrame)desktopPane.getSelectedFrame()).setInputEnabled(false);
            input.addInput(((FileFrame)desktopPane.getSelectedFrame()).getInputFieldText() + "\n");
            ((FileFrame)desktopPane.getSelectedFrame()).clearInputField();
        }
    };
    
    /**
     * Used to control running programs - only one TAM program can be run at once
     */
    ActionListener delegateRun = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            runMenuItem.setEnabled(true);
            buttonRun.setEnabled(true);
            compileMenuItem.setEnabled(true);
            buttonCompile.setEnabled(true);
        }
    };
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" GUI Variables ">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton LLVM;
    javax.swing.JMenuItem aboutMenuItem;
    javax.swing.JButton buttonCompile;
    javax.swing.JButton buttonCopy;
    javax.swing.JButton buttonCut;
    javax.swing.JButton buttonNew;
    javax.swing.JButton buttonOpen;
    javax.swing.JButton buttonPaste;
    javax.swing.JButton buttonRun;
    javax.swing.JButton buttonSave;
    javax.swing.JMenuItem compileMenuItem;
    javax.swing.JMenuItem copyMenuItem;
    javax.swing.JMenuItem cutMenuItem;
    javax.swing.JDesktopPane desktopPane;
    javax.swing.JMenu editMenu;
    javax.swing.JToolBar editToolBar;
    javax.swing.JMenuItem exitMenuItem;
    javax.swing.JMenu fileMenu;
    javax.swing.JToolBar fileToolBar;
    javax.swing.JMenu helpMenu;
    javax.swing.JMenuBar menuBar;
    javax.swing.JMenuItem newMenuItem;
    javax.swing.JMenuItem openMenuItem;
    javax.swing.JMenuItem pasteMenuItem;
    javax.swing.JMenuItem runMenuItem;
    javax.swing.JMenuItem saveAsMenuItem;
    javax.swing.JMenuItem saveMenuItem;
    javax.swing.JSeparator separatorExit;
    javax.swing.JPanel toolBarsPanel;
    javax.swing.JMenu triangleMenu;
    javax.swing.JToolBar triangleToolBar;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Non-GUI Variables ">
    // [ Non-GUI variables declaration ]
    int untitledCount = 1;                                                  // Counts "Untitled" document names (e.g. "Untitled-1")
    clipBoard Clip = new clipBoard();                                       // Clipboard Management
    IDECompiler compiler = new IDECompiler();                               // Compiler - Analyzes/generates TAM programs
    IDEDisassembler disassembler = new IDEDisassembler();                   // Disassembler - Generates TAM Code
    IDEInterpreter interpreter = new IDEInterpreter(delegateRun);           // Interpreter - Runs TAM programs
    OutputRedirector output = new OutputRedirector();                       // Redirects the console output
    InputRedirector input = new InputRedirector(delegateInput);             // Redirects console input
    TreeVisitor treeVisitor = new TreeVisitor();                            // Draws the Abstract Syntax Trees
    TableVisitor tableVisitor = new TableVisitor();                         // Draws the Identifier Table
    File directory;                                                         // The current directory.
    // [ End of Non-GUI variables declaration ]
    // </editor-fold>    
    
    // <editor-fold defaultstate="collapsed" desc=" Internal Class - ClipboardOwner ">
    /**
     * ClipboardOwner Class
     * Internal clipboard management class, uses the default system clipboard.
     */
    private class clipBoard implements ClipboardOwner {
        
        /**
         * Required by interface - not implemented
         */
        public void lostOwnership(Clipboard aClipboard, Transferable aContents) { }
        
        /**
         * Sets the clipboard contents
         */
        public void setClipboardContents(String _contents) {
            StringSelection stringSelection = new StringSelection(_contents);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, this);
        }
        
        /**
         * Returns the clipboard contents
         */
        public String getClipboardContents() {
            String ret = "";
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = clipboard.getContents(null);
            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    DataFlavor flavorSet[] = contents.getTransferDataFlavors();
                    boolean canString = false;
                    for (int i=0;i<flavorSet.length;i++)
                        if (flavorSet[i] == DataFlavor.stringFlavor)
                            canString = true;
                    
                    ret = (String)contents.getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) { }
            }
            return(ret);
        }        
    }
    
    // </editor-fold>
}
