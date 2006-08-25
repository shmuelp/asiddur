/*
 * HelloMidlet.java
 *
 * Created on February 5, 2006, 5:31 AM
 */

package com.saraandshmuel.asiddur.midpui;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author shmuelp
 */
public class ASiddurMidlet extends MIDlet implements CommandListener {
    
    /** Creates a new instance of HelloMidlet */
    public ASiddurMidlet() {
        System.out.println("ASiddurMidlet is being constructed");
        System.out.flush();
        mediator.chooseText(-1);
    }

    private Form MainForm;//GEN-BEGIN:MVDFields
    private StringItem topStringItem;
    private Command exitCommand;
    private Spacer spacer1;
    private DateField useDateField;
    private Spacer spacer2;
    private ChoiceGroup tefillaChoiceGroup;
    private Command daavenCommand;
    private Command menuCommand;
    private com.saraandshmuel.asiddur.midpui.HebrewTextCanvas daavenCanvas;
    private Command debugOutputCommand;
    private Command helpCommand;
    private Form helpForm;
    private Command aboutCommand;
    private StringItem helpDescription;
    private Form settingsForm;
    private StringItem settingsDescription;
    private Command settingsCommand;
    private Command todoCommand;
    private Command helpCommand1;
    private Form todoForm;
    private Command backToHelpCommand;
    private StringItem stringItem1;
    private Command clearCommand;
    private Form aboutForm;
    private StringItem aboutString;
    private Form debugOutputForm;
    private Command backToMainCommand;
    private StringItem debugOutput;
    private Spacer spacer3;
    private StringItem hebrewDateString;
    private com.saraandshmuel.asiddur.midpui.TestCanvas testCanvas;
    private Command testCommand;
    private Form showChoicesForm;
    private StringItem choiceString;
    private Command summaryCommand;
    private Command testCommand1;
    private ChoiceGroup fontChoiceGroup;
    private Spacer spacer4;//GEN-END:MVDFields
//    private char[] testText;
    
    private MidpMediator mediator = new MidpMediator(this);
    
//GEN-LINE:MVDMethods

    /** This method initializes UI of the application.//GEN-BEGIN:MVDInitBegin
     */
    private void initialize() {//GEN-END:MVDInitBegin
        // Insert pre-init code here
       spacer1 = new Spacer(1000, 1);//GEN-BEGIN:MVDInitInit
       useDateField = new DateField("Set date/time:", DateField.DATE_TIME);
       fontChoiceGroup = new ChoiceGroup("Choose font:", Choice.POPUP, new String[] {
          "Native",
          "Native (reversed)",
          "Nachlieli-20",
          "Miriam-22"
       }, new Image[] {
          null,
          null,
          null,
          null
       });
       fontChoiceGroup.setSelectedFlags(new boolean[] {
          false,
          false,
          true,
          false
       });
       tefillaChoiceGroup = new ChoiceGroup("", Choice.POPUP, new String[0], new Image[0]);
       tefillaChoiceGroup.setSelectedFlags(new boolean[0]);
       spacer4 = new Spacer(1000, 1);
       topStringItem = new StringItem("A Siddur v 0.1+", "A soon-to-be intelligent siddur");
       daavenCanvas = new com.saraandshmuel.asiddur.midpui.HebrewTextCanvas("");
       daavenCanvas.addCommand(get_backToMainCommand());
       daavenCanvas.addCommand(get_debugOutputCommand());
       daavenCanvas.addCommand(get_exitCommand());
       daavenCanvas.setCommandListener(this);
       spacer2 = new Spacer(1000, 1);
       spacer3 = new Spacer(1000, 1);
       getDisplay().setCurrent(get_MainForm());//GEN-END:MVDInitInit
        // Insert post-init code here
        daavenCanvas.setMediator(mediator);
        final String[] texts = mediator.getTextLabels();
        for (int i = 0; i < texts.length; i++) {
            tefillaChoiceGroup.append(texts[i], null);
        }
        get_MainForm().setItemStateListener(mediator);
        mediator.setDate(new java.util.Date());
        //mediator.chooseText(1);
    }//GEN-LINE:MVDInitEnd
    
    /** Called by the system to indicate that a command has been invoked on a particular displayable.//GEN-BEGIN:MVDCABegin
     * @param command the Command that ws invoked
     * @param displayable the Displayable on which the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:MVDCABegin
        // Insert global pre-action code here
       if (displayable == MainForm) {//GEN-BEGIN:MVDCABody
          if (command == exitCommand) {//GEN-END:MVDCABody
                // Insert pre-action code here
             exitMIDlet();//GEN-LINE:MVDCAAction3
                // Insert post-action code here
          } else if (command == daavenCommand) {//GEN-LINE:MVDCACase3
                // Insert pre-action code here
                mediator.chooseText(tefillaChoiceGroup.getSelectedIndex());
                getDisplay().setCurrent(daavenCanvas);//GEN-LINE:MVDCAAction31
                // Insert post-action code here
          } else if (command == debugOutputCommand) {//GEN-LINE:MVDCACase31
                // Insert pre-action code here
             getDisplay().setCurrent(get_debugOutputForm());//GEN-LINE:MVDCAAction51
                // Insert post-action code here
          } else if (command == helpCommand) {//GEN-LINE:MVDCACase51
                // Insert pre-action code here
             getDisplay().setCurrent(get_helpForm());//GEN-LINE:MVDCAAction63
                // Insert post-action code here
          } else if (command == settingsCommand) {//GEN-LINE:MVDCACase63
                // Insert pre-action code here
             getDisplay().setCurrent(get_settingsForm());//GEN-LINE:MVDCAAction75
                // Insert post-action code here
          } else if (command == summaryCommand) {//GEN-LINE:MVDCACase75
                // Insert pre-action code here
             getDisplay().setCurrent(get_showChoicesForm());//GEN-LINE:MVDCAAction135
                // Insert post-action code here
          } else if (command == testCommand1) {//GEN-LINE:MVDCACase135
             // Insert pre-action code here
             getDisplay().setCurrent(get_testCanvas());//GEN-LINE:MVDCAAction139
             // Insert post-action code here
          }//GEN-BEGIN:MVDCACase139
       } else if (displayable == daavenCanvas) {
          if (command == exitCommand) {//GEN-END:MVDCACase139
                // Insert pre-action code here
             exitMIDlet();//GEN-LINE:MVDCAAction45
                // Insert post-action code here
          } else if (command == backToMainCommand) {//GEN-LINE:MVDCACase45
                // Insert pre-action code here
             getDisplay().setCurrent(get_MainForm());//GEN-LINE:MVDCAAction108
                // Insert post-action code here
          } else if (command == debugOutputCommand) {//GEN-LINE:MVDCACase108
                // Insert pre-action code here
             getDisplay().setCurrent(get_debugOutputForm());//GEN-LINE:MVDCAAction109
                // Insert post-action code here
          }//GEN-BEGIN:MVDCACase109
       } else if (displayable == helpForm) {
          if (command == aboutCommand) {//GEN-END:MVDCACase109
                // Insert pre-action code here
             getDisplay().setCurrent(get_aboutForm());//GEN-LINE:MVDCAAction68
                // Insert post-action code here
          } else if (command == exitCommand) {//GEN-LINE:MVDCACase68
                // Insert pre-action code here
             exitMIDlet();//GEN-LINE:MVDCAAction65
                // Insert post-action code here
          } else if (command == todoCommand) {//GEN-LINE:MVDCACase65
                // Insert pre-action code here
             getDisplay().setCurrent(get_todoForm());//GEN-LINE:MVDCAAction80
                // Insert post-action code here
          } else if (command == backToMainCommand) {//GEN-LINE:MVDCACase80
                // Insert pre-action code here
             getDisplay().setCurrent(get_MainForm());//GEN-LINE:MVDCAAction106
                // Insert post-action code here
          }//GEN-BEGIN:MVDCACase106
       } else if (displayable == settingsForm) {
          if (command == exitCommand) {//GEN-END:MVDCACase106
                // Insert pre-action code here
             exitMIDlet();//GEN-LINE:MVDCAAction76
                // Insert post-action code here
          } else if (command == backToMainCommand) {//GEN-LINE:MVDCACase76
                // Insert pre-action code here
             getDisplay().setCurrent(get_MainForm());//GEN-LINE:MVDCAAction107
                // Insert post-action code here
          }//GEN-BEGIN:MVDCACase107
       } else if (displayable == todoForm) {
          if (command == backToHelpCommand) {//GEN-END:MVDCACase107
                // Insert pre-action code here
             getDisplay().setCurrent(get_helpForm());//GEN-LINE:MVDCAAction90
                // Insert post-action code here
          } else if (command == exitCommand) {//GEN-LINE:MVDCACase90
                // Insert pre-action code here
             exitMIDlet();//GEN-LINE:MVDCAAction92
                // Insert post-action code here
          }//GEN-BEGIN:MVDCACase92
       } else if (displayable == aboutForm) {
          if (command == backToHelpCommand) {//GEN-END:MVDCACase92
                // Insert pre-action code here
             getDisplay().setCurrent(get_helpForm());//GEN-LINE:MVDCAAction97
                // Insert post-action code here
          } else if (command == exitCommand) {//GEN-LINE:MVDCACase97
                // Insert pre-action code here
             exitMIDlet();//GEN-LINE:MVDCAAction98
                // Insert post-action code here
          }//GEN-BEGIN:MVDCACase98
       } else if (displayable == debugOutputForm) {
          if (command == backToMainCommand) {//GEN-END:MVDCACase98
                // Insert pre-action code here
             getDisplay().setCurrent(get_MainForm());//GEN-LINE:MVDCAAction102
                // Insert post-action code here
          } else if (command == exitCommand) {//GEN-LINE:MVDCACase102
                // Insert pre-action code here
             exitMIDlet();//GEN-LINE:MVDCAAction104
                // Insert post-action code here
          } else if (command == clearCommand) {//GEN-LINE:MVDCACase104
                // Insert pre-action code here
             // Do nothing//GEN-LINE:MVDCAAction103
                // Insert post-action code here
                get_debugOutput().setText("");
          }//GEN-BEGIN:MVDCACase103
       } else if (displayable == testCanvas) {
          if (command == backToMainCommand) {//GEN-END:MVDCACase103
                // Insert pre-action code here
             getDisplay().setCurrent(get_MainForm());//GEN-LINE:MVDCAAction125
                // Insert post-action code here
          } else if (command == exitCommand) {//GEN-LINE:MVDCACase125
                // Insert pre-action code here
             exitMIDlet();//GEN-LINE:MVDCAAction127
                // Insert post-action code here
          } else if (command == debugOutputCommand) {//GEN-LINE:MVDCACase127
                // Insert pre-action code here
             getDisplay().setCurrent(get_debugOutputForm());//GEN-LINE:MVDCAAction126
                // Insert post-action code here
          }//GEN-BEGIN:MVDCACase126
       } else if (displayable == showChoicesForm) {
          if (command == backToMainCommand) {//GEN-END:MVDCACase126
                // Insert pre-action code here
             getDisplay().setCurrent(get_MainForm());//GEN-LINE:MVDCAAction130
                // Insert post-action code here
          } else if (command == exitCommand) {//GEN-LINE:MVDCACase130
                // Insert pre-action code here
             exitMIDlet();//GEN-LINE:MVDCAAction131
                // Insert post-action code here
          } else if (command == debugOutputCommand) {//GEN-LINE:MVDCACase131
                // Insert pre-action code here
             getDisplay().setCurrent(get_debugOutputForm());//GEN-LINE:MVDCAAction132
                // Insert post-action code here
          }//GEN-BEGIN:MVDCACase132
       }//GEN-END:MVDCACase132
        // Insert global post-action code here
}//GEN-LINE:MVDCAEnd
    
    /**
     * This method should return an instance of the display.
     */
    public Display getDisplay() {//GEN-FIRST:MVDGetDisplay
        return Display.getDisplay(this);
    }//GEN-LAST:MVDGetDisplay
    
    /**
     * This method should exit the midlet.
     */
    public void exitMIDlet() {//GEN-FIRST:MVDExitMidlet
        getDisplay().setCurrent(null);
        destroyApp(true);
        notifyDestroyed();
    }//GEN-LAST:MVDExitMidlet
    
    
    
 
   
   

    /** This method returns instance for menuCommand component and should be called instead of accessing menuCommand field directly.//GEN-BEGIN:MVDGetBegin32
     * @return Instance for menuCommand component
     */
    public Command get_menuCommand() {
       if (menuCommand == null) {//GEN-END:MVDGetBegin32
            // Insert pre-init code here
          menuCommand = new Command("Menu", Command.SCREEN, 1);//GEN-LINE:MVDGetInit32
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd32
       return menuCommand;
    }//GEN-END:MVDGetEnd32

    /** This method returns instance for MainForm component and should be called instead of accessing MainForm field directly.//GEN-BEGIN:MVDGetBegin2
     * @return Instance for MainForm component
     */
    public Form get_MainForm() {
       if (MainForm == null) {//GEN-END:MVDGetBegin2
            // Insert pre-init code here
          MainForm = new Form(null, new Item[] {//GEN-BEGIN:MVDGetInit2
             topStringItem,
             spacer1,
             get_hebrewDateString(),
             spacer2,
             tefillaChoiceGroup,
             spacer3,
             fontChoiceGroup,
             spacer4,
             useDateField
          });
          MainForm.addCommand(get_daavenCommand());
          MainForm.addCommand(get_settingsCommand());
          MainForm.addCommand(get_debugOutputCommand());
          MainForm.addCommand(get_helpCommand());
          MainForm.addCommand(get_exitCommand());
          MainForm.addCommand(get_summaryCommand());
          MainForm.addCommand(get_testCommand1());
          MainForm.setCommandListener(this);//GEN-END:MVDGetInit2
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd2
       return MainForm;
    }//GEN-END:MVDGetEnd2

    /** This method returns instance for exitCommand component and should be called instead of accessing exitCommand field directly.//GEN-BEGIN:MVDGetBegin5
     * @return Instance for exitCommand component
     */
    public Command get_exitCommand() {
       if (exitCommand == null) {//GEN-END:MVDGetBegin5
            // Insert pre-init code here
          exitCommand = new Command("Exit", Command.EXIT, 1);//GEN-LINE:MVDGetInit5
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd5
       return exitCommand;
    }//GEN-END:MVDGetEnd5

    /** This method returns instance for daavenCommand component and should be called instead of accessing daavenCommand field directly.//GEN-BEGIN:MVDGetBegin30
     * @return Instance for daavenCommand component
     */
    public Command get_daavenCommand() {
       if (daavenCommand == null) {//GEN-END:MVDGetBegin30
            // Insert pre-init code here
          daavenCommand = new Command("Daaven", Command.SCREEN, 1);//GEN-LINE:MVDGetInit30
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd30
       return daavenCommand;
    }//GEN-END:MVDGetEnd30

    /** This method returns instance for debugOutputCommand component and should be called instead of accessing debugOutputCommand field directly.//GEN-BEGIN:MVDGetBegin50
     * @return Instance for debugOutputCommand component
     */
    public Command get_debugOutputCommand() {
       if (debugOutputCommand == null) {//GEN-END:MVDGetBegin50
            // Insert pre-init code here
          debugOutputCommand = new Command("Log", Command.SCREEN, 1);//GEN-LINE:MVDGetInit50
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd50
       return debugOutputCommand;
    }//GEN-END:MVDGetEnd50
  
    /** This method returns instance for helpCommand component and should be called instead of accessing helpCommand field directly.//GEN-BEGIN:MVDGetBegin62
     * @return Instance for helpCommand component
     */
    public Command get_helpCommand() {
       if (helpCommand == null) {//GEN-END:MVDGetBegin62
            // Insert pre-init code here
          helpCommand = new Command("Help", Command.SCREEN, 1);//GEN-LINE:MVDGetInit62
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd62
       return helpCommand;
    }//GEN-END:MVDGetEnd62

    /** This method returns instance for helpForm component and should be called instead of accessing helpForm field directly.//GEN-BEGIN:MVDGetBegin64
     * @return Instance for helpForm component
     */
    public Form get_helpForm() {
       if (helpForm == null) {//GEN-END:MVDGetBegin64
            // Insert pre-init code here
          helpForm = new Form(null, new Item[] {get_helpDescription()});//GEN-BEGIN:MVDGetInit64
          helpForm.addCommand(get_backToMainCommand());
          helpForm.addCommand(get_aboutCommand());
          helpForm.addCommand(get_todoCommand());
          helpForm.addCommand(get_exitCommand());
          helpForm.setCommandListener(this);//GEN-END:MVDGetInit64
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd64
       return helpForm;
    }//GEN-END:MVDGetEnd64

    /** This method returns instance for aboutCommand component and should be called instead of accessing aboutCommand field directly.//GEN-BEGIN:MVDGetBegin67
     * @return Instance for aboutCommand component
     */
    public Command get_aboutCommand() {
       if (aboutCommand == null) {//GEN-END:MVDGetBegin67
            // Insert pre-init code here
          aboutCommand = new Command("About", Command.SCREEN, 1);//GEN-LINE:MVDGetInit67
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd67
       return aboutCommand;
    }//GEN-END:MVDGetEnd67

    /** This method returns instance for helpDescription component and should be called instead of accessing helpDescription field directly.//GEN-BEGIN:MVDGetBegin71
     * @return Instance for helpDescription component
     */
    public StringItem get_helpDescription() {
       if (helpDescription == null) {//GEN-END:MVDGetBegin71
            // Insert pre-init code here
          helpDescription = new StringItem("Help\n", "\nUse the commands of this screen to get help, view the readme file, see the license.\n\nASiddur attempts to be an intelligent siddur.  By knowing the current time and date, it is able to guess what tefilla to display and the correct text of the tefilla.  It should even be able to highlight those portions of the tefilla that are different, helping to keep you from forgetting key changes.\n\nNote: ASiddur is a work in progress.");//GEN-LINE:MVDGetInit71
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd71
       return helpDescription;
    }//GEN-END:MVDGetEnd71

    /** This method returns instance for settingsForm component and should be called instead of accessing settingsForm field directly.//GEN-BEGIN:MVDGetBegin72
     * @return Instance for settingsForm component
     */
    public Form get_settingsForm() {
       if (settingsForm == null) {//GEN-END:MVDGetBegin72
            // Insert pre-init code here
          settingsForm = new Form(null, new Item[] {get_settingsDescription()});//GEN-BEGIN:MVDGetInit72
          settingsForm.addCommand(get_backToMainCommand());
          settingsForm.addCommand(get_exitCommand());
          settingsForm.setCommandListener(this);//GEN-END:MVDGetInit72
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd72
       return settingsForm;
    }//GEN-END:MVDGetEnd72

    /** This method returns instance for settingsDescription component and should be called instead of accessing settingsDescription field directly.//GEN-BEGIN:MVDGetBegin73
     * @return Instance for settingsDescription component
     */
    public StringItem get_settingsDescription() {
       if (settingsDescription == null) {//GEN-END:MVDGetBegin73
            // Insert pre-init code here
          settingsDescription = new StringItem("Settings\n", "\nDescription of settings goes here.  Actual settings go in the commands of this screen.\n\nNote: support for settings is currently planned for version 0.3.\n");//GEN-LINE:MVDGetInit73
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd73
       return settingsDescription;
    }//GEN-END:MVDGetEnd73

    /** This method returns instance for settingsCommand component and should be called instead of accessing settingsCommand field directly.//GEN-BEGIN:MVDGetBegin74
     * @return Instance for settingsCommand component
     */
    public Command get_settingsCommand() {
       if (settingsCommand == null) {//GEN-END:MVDGetBegin74
            // Insert pre-init code here
          settingsCommand = new Command("Prefs", Command.SCREEN, 1);//GEN-LINE:MVDGetInit74
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd74
       return settingsCommand;
    }//GEN-END:MVDGetEnd74
 
    /** This method returns instance for todoCommand component and should be called instead of accessing todoCommand field directly.//GEN-BEGIN:MVDGetBegin79
     * @return Instance for todoCommand component
     */
    public Command get_todoCommand() {
       if (todoCommand == null) {//GEN-END:MVDGetBegin79
            // Insert pre-init code here
          todoCommand = new Command("Todo", Command.SCREEN, 1);//GEN-LINE:MVDGetInit79
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd79
       return todoCommand;
    }//GEN-END:MVDGetEnd79

    /** This method returns instance for helpCommand1 component and should be called instead of accessing helpCommand1 field directly.//GEN-BEGIN:MVDGetBegin86
     * @return Instance for helpCommand1 component
     */
    public Command get_helpCommand1() {
       if (helpCommand1 == null) {//GEN-END:MVDGetBegin86
            // Insert pre-init code here
          helpCommand1 = new Command("Help", Command.HELP, 1);//GEN-LINE:MVDGetInit86
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd86
       return helpCommand1;
    }//GEN-END:MVDGetEnd86

    /** This method returns instance for todoForm component and should be called instead of accessing todoForm field directly.//GEN-BEGIN:MVDGetBegin88
     * @return Instance for todoForm component
     */
    public Form get_todoForm() {
       if (todoForm == null) {//GEN-END:MVDGetBegin88
            // Insert pre-init code here
          todoForm = new Form(null, new Item[] {get_stringItem1()});//GEN-BEGIN:MVDGetInit88
          todoForm.addCommand(get_backToHelpCommand());
          todoForm.addCommand(get_exitCommand());
          todoForm.setCommandListener(this);//GEN-END:MVDGetInit88
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd88
       return todoForm;
    }//GEN-END:MVDGetEnd88

    /** This method returns instance for backToHelpCommand component and should be called instead of accessing backToHelpCommand field directly.//GEN-BEGIN:MVDGetBegin89
     * @return Instance for backToHelpCommand component
     */
    public Command get_backToHelpCommand() {
       if (backToHelpCommand == null) {//GEN-END:MVDGetBegin89
            // Insert pre-init code here
          backToHelpCommand = new Command("< Help", Command.BACK, 1);//GEN-LINE:MVDGetInit89
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd89
       return backToHelpCommand;
    }//GEN-END:MVDGetEnd89

    /** This method returns instance for stringItem1 component and should be called instead of accessing stringItem1 field directly.//GEN-BEGIN:MVDGetBegin91
     * @return Instance for stringItem1 component
     */
    public StringItem get_stringItem1() {
       if (stringItem1 == null) {//GEN-END:MVDGetBegin91
            // Insert pre-init code here
          stringItem1 = new StringItem("", "The feature plan can be found using the issue tracker available at:\nhttp://code.google.com/");//GEN-LINE:MVDGetInit91
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd91
       return stringItem1;
    }//GEN-END:MVDGetEnd91

    /** This method returns instance for clearCommand component and should be called instead of accessing clearCommand field directly.//GEN-BEGIN:MVDGetBegin94
     * @return Instance for clearCommand component
     */
    public Command get_clearCommand() {
       if (clearCommand == null) {//GEN-END:MVDGetBegin94
            // Insert pre-init code here
          clearCommand = new Command("Clear", Command.ITEM, 1);//GEN-LINE:MVDGetInit94
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd94
       return clearCommand;
    }//GEN-END:MVDGetEnd94

    /** This method returns instance for aboutForm component and should be called instead of accessing aboutForm field directly.//GEN-BEGIN:MVDGetBegin96
     * @return Instance for aboutForm component
     */
    public Form get_aboutForm() {
       if (aboutForm == null) {//GEN-END:MVDGetBegin96
            // Insert pre-init code here
          aboutForm = new Form(null, new Item[] {get_aboutString()});//GEN-BEGIN:MVDGetInit96
          aboutForm.addCommand(get_backToHelpCommand());
          aboutForm.addCommand(get_exitCommand());
          aboutForm.setCommandListener(this);//GEN-END:MVDGetInit96
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd96
       return aboutForm;
    }//GEN-END:MVDGetEnd96

    /** This method returns instance for aboutString component and should be called instead of accessing aboutString field directly.//GEN-BEGIN:MVDGetBegin99
     * @return Instance for aboutString component
     */
    public StringItem get_aboutString() {
       if (aboutString == null) {//GEN-END:MVDGetBegin99
            // Insert pre-init code here
          aboutString = new StringItem("ASiddur 0.1+\n", "\n(c) 2006 S. Popper\n\nPortions (c) Avrom Finkelstein\n\nThanks to:\nSara P.\nAvi P.\nJoe B.");//GEN-LINE:MVDGetInit99
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd99
       return aboutString;
    }//GEN-END:MVDGetEnd99

    /** This method returns instance for debugOutputForm component and should be called instead of accessing debugOutputForm field directly.//GEN-BEGIN:MVDGetBegin100
     * @return Instance for debugOutputForm component
     */
    public Form get_debugOutputForm() {
       if (debugOutputForm == null) {//GEN-END:MVDGetBegin100
            // Insert pre-init code here
          debugOutputForm = new Form(null, new Item[] {get_debugOutput()});//GEN-BEGIN:MVDGetInit100
          debugOutputForm.addCommand(get_backToMainCommand());
          debugOutputForm.addCommand(get_clearCommand());
          debugOutputForm.addCommand(get_exitCommand());
          debugOutputForm.setCommandListener(this);//GEN-END:MVDGetInit100
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd100
       return debugOutputForm;
    }//GEN-END:MVDGetEnd100

    /** This method returns instance for backToMainCommand component and should be called instead of accessing backToMainCommand field directly.//GEN-BEGIN:MVDGetBegin101
     * @return Instance for backToMainCommand component
     */
    public Command get_backToMainCommand() {
       if (backToMainCommand == null) {//GEN-END:MVDGetBegin101
            // Insert pre-init code here
          backToMainCommand = new Command("< Main menu", Command.BACK, 1);//GEN-LINE:MVDGetInit101
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd101
       return backToMainCommand;
    }//GEN-END:MVDGetEnd101

    /** This method returns instance for debugOutput component and should be called instead of accessing debugOutput field directly.//GEN-BEGIN:MVDGetBegin105
     * @return Instance for debugOutput component
     */
    public StringItem get_debugOutput() {
       if (debugOutput == null) {//GEN-END:MVDGetBegin105
            // Insert pre-init code here
          debugOutput = new StringItem("", "");//GEN-LINE:MVDGetInit105
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd105
       return debugOutput;
    }//GEN-END:MVDGetEnd105

    /** This method returns instance for hebrewDateString component and should be called instead of accessing hebrewDateString field directly.//GEN-BEGIN:MVDGetBegin113
     * @return Instance for hebrewDateString component
     */
    public StringItem get_hebrewDateString() {
       if (hebrewDateString == null) {//GEN-END:MVDGetBegin113
            // Insert pre-init code here
          hebrewDateString = new StringItem(null, "");//GEN-BEGIN:MVDGetInit113
          hebrewDateString.setLayout(Item.LAYOUT_CENTER);//GEN-END:MVDGetInit113
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd113
       return hebrewDateString;
    }//GEN-END:MVDGetEnd113
   
    /** This method returns instance for testCanvas component and should be called instead of accessing testCanvas field directly.//GEN-BEGIN:MVDGetBegin122
     * @return Instance for testCanvas component
     */
    public com.saraandshmuel.asiddur.midpui.TestCanvas get_testCanvas() {
       if (testCanvas == null) {//GEN-END:MVDGetBegin122
            // Insert pre-init code here
          testCanvas = new com.saraandshmuel.asiddur.midpui.TestCanvas();//GEN-BEGIN:MVDGetInit122
          testCanvas.addCommand(get_backToMainCommand());
          testCanvas.addCommand(get_debugOutputCommand());
          testCanvas.addCommand(get_exitCommand());
          testCanvas.setCommandListener(this);//GEN-END:MVDGetInit122
            // Insert post-init code here
          testCanvas.setMediator(mediator);
       }//GEN-BEGIN:MVDGetEnd122
       return testCanvas;
    }//GEN-END:MVDGetEnd122

    /** This method returns instance for testCommand component and should be called instead of accessing testCommand field directly.//GEN-BEGIN:MVDGetBegin123
     * @return Instance for testCommand component
     */
    public Command get_testCommand() {
       if (testCommand == null) {//GEN-END:MVDGetBegin123
            // Insert pre-init code here
          testCommand = new Command("Test", Command.SCREEN, 1);//GEN-LINE:MVDGetInit123
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd123
       return testCommand;
    }//GEN-END:MVDGetEnd123

    /** This method returns instance for showChoicesForm component and should be called instead of accessing showChoicesForm field directly.//GEN-BEGIN:MVDGetBegin129
     * @return Instance for showChoicesForm component
     */
    public Form get_showChoicesForm() {
       if (showChoicesForm == null) {//GEN-END:MVDGetBegin129
            // Insert pre-init code here
          showChoicesForm = new Form(null, new Item[] {get_choiceString()});//GEN-BEGIN:MVDGetInit129
          showChoicesForm.addCommand(get_backToMainCommand());
          showChoicesForm.addCommand(get_debugOutputCommand());
          showChoicesForm.addCommand(get_exitCommand());
          showChoicesForm.setCommandListener(this);//GEN-END:MVDGetInit129
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd129
       return showChoicesForm;
    }//GEN-END:MVDGetEnd129

    /** This method returns instance for choiceString component and should be called instead of accessing choiceString field directly.//GEN-BEGIN:MVDGetBegin133
     * @return Instance for choiceString component
     */
    public StringItem get_choiceString() {
       if (choiceString == null) {//GEN-END:MVDGetBegin133
            // Insert pre-init code here
          choiceString = new StringItem("DEBUG: tefillah choice functions:\nTODO: Check correctness, fix tacahanun\nTODO (after prefs) - allow for e.g. Israel rules\n", "");//GEN-LINE:MVDGetInit133
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd133
       return choiceString;
    }//GEN-END:MVDGetEnd133

    /** This method returns instance for summaryCommand component and should be called instead of accessing summaryCommand field directly.//GEN-BEGIN:MVDGetBegin134
     * @return Instance for summaryCommand component
     */
    public Command get_summaryCommand() {
       if (summaryCommand == null) {//GEN-END:MVDGetBegin134
            // Insert pre-init code here
          summaryCommand = new Command("Summary", "Summary of Tefilla Choices", Command.SCREEN, 1);//GEN-LINE:MVDGetInit134
            // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd134
       return summaryCommand;
    }//GEN-END:MVDGetEnd134

    /** This method returns instance for testCommand1 component and should be called instead of accessing testCommand1 field directly.//GEN-BEGIN:MVDGetBegin138
     * @return Instance for testCommand1 component
     */
    public Command get_testCommand1() {
       if (testCommand1 == null) {//GEN-END:MVDGetBegin138
          // Insert pre-init code here
          testCommand1 = new Command("Test", Command.SCREEN, 1);//GEN-LINE:MVDGetInit138
          // Insert post-init code here
       }//GEN-BEGIN:MVDGetEnd138
       return testCommand1;
    }//GEN-END:MVDGetEnd138
   
 
    public void startApp() {
        System.out.println("ASiddurMidlet is being started");
        System.out.flush();
        initialize();
        System.out.println("ASiddurMidlet: start complete");
        System.out.flush();
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
       mediator.releaseReferences();
       System.gc();
    }
    
    public DateField getUseDateField() {
        return useDateField;
    }

    public com.saraandshmuel.asiddur.midpui.HebrewTextCanvas getDaavenCanvas() {
        return daavenCanvas;
    }

    public ChoiceGroup getTefillaChoiceGroup() {
        return tefillaChoiceGroup;
    }
    
   public ChoiceGroup getFontChoiceGroup() {
      return fontChoiceGroup;
   }

    /**
     * Sets all references to null.  Needed to ensure that memory is released 
     * back to system for PalmOS.  See post at: 
     * http://news.palmos.com/read/messages?id=204129
     */
    public void releaseReferences() {
       this.MainForm = null;
       this.aboutCommand = null;
       this.aboutString = null;
       this.backToHelpCommand = null;
       this.backToMainCommand = null;
       this.choiceString = null;
       this.clearCommand = null;
       if ( this.daavenCanvas != null) { this.daavenCanvas.releaseReferences();}
       this.daavenCanvas = null;
       this.daavenCommand = null;
       this.debugOutput = null;
       this.debugOutputCommand = null;
       this.debugOutputForm = null;
       this.exitCommand = null;
       this.fontChoiceGroup = null;
       this.hebrewDateString = null;
       this.helpCommand = null;
       this.helpCommand1 = null;
       this.helpDescription = null;
       this.helpForm = null;
       this.mediator = null;
       this.menuCommand = null;
       this.settingsCommand = null;
       this.settingsDescription = null;
       this.settingsForm = null;
       this.showChoicesForm = null;
       this.spacer1 = null;
       this.spacer2 = null;
       this.spacer3 = null;
       this.spacer4 = null;
       this.stringItem1 = null;
       this.summaryCommand = null;
       this.tefillaChoiceGroup = null;
       if ( this.testCanvas != null ) { this.testCanvas.releaseReferences(); }
       this.testCanvas = null;
       this.testCommand = null;
       this.testCommand1 = null;
       this.todoCommand = null;
       this.todoForm = null;
       this.topStringItem = null;
       this.useDateField = null;
    }
}
