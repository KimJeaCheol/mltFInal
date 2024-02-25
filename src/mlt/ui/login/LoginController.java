package mlt.ui.login;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import mlt.db.DbHandler;
import mlt.ui.Dashboard.Controller;
import mlt.ui.Dashboard.teacher.DashboardTeacherController;
import mlt.ui.MyAlert.MyAlert;
import mlt.ui.login.logic.SHAExample;
import mlt.ui.support.CloseStage;
import mlt.ui.support.CreateStage;
import mlt.ui.support.GetStage;

public class LoginController implements Initializable {


//  login Controls

    @FXML
    private JFXTextField txtuname;

    @FXML
    private JFXPasswordField txtpassword;

    @FXML
    private JFXButton btnlogin;

    @FXML
    private JFXButton btnadminlogin;

    @FXML
    private JFXButton btnforget;

    @FXML
    private JFXButton btnshowpassword;

    @FXML
    private JFXButton btnshowpassword1;

    @FXML
    private JFXButton btnteacherlogin;

    @FXML
    private JFXButton close;

    @FXML
    private BorderPane mainborderpane;

    @FXML
    private Label lblshowpassword;


    @FXML
    private AnchorPane panelogin;


    @FXML
    private AnchorPane paneerror;

    @FXML
    private AnchorPane panedbconnect;

     @FXML
    private AnchorPane paneremotedbconnect;


//     For custom Localhost use

    @FXML
    private JFXTextField txtdbuname;


    @FXML
    private JFXPasswordField txtdbpass;

//    For remote connection

    @FXML
    private JFXTextField txtrdbuname;


    @FXML
    private JFXPasswordField txtrdbpass;


    @FXML
    private JFXTextField txtdbconString;

    @FXML
    private Pane pane1;
    
    @FXML
    private Pane pane2;
    
    @FXML
    private Pane pane3;
    
    @FXML
    private Pane pane4;

    private static Connection con = null;

    private boolean showpass = false;

    private boolean tlogin = true;

    String salt = null;


    @FXML
    private JFXToggleButton tglblacktheme;


    @FXML
    private FontAwesomeIconView glyusers;

    @FXML
    private FontAwesomeIconView glyuser;
    private String theme;


//    On forget button click

    @FXML
    void forget(ActionEvent event) {
        if (tglblacktheme.isSelected())
            new CreateStage().createStage("Forget Password","/mlt/ui/login/precovery.fxml",null,0,true,false,false,"black");
        else
            new CreateStage().createStage("Forget Password","/mlt/ui/login/precovery.fxml",null,0,true,false,false,"white");
    }

    @FXML
    void login(ActionEvent event) {

        try{
            txtuname.getStyleClass().remove("wrong-credential");
            txtpassword.getStyleClass().remove("wrong-credential");
            if (!(txtuname.getText().equals("") || txtpassword.getText().equals(""))){

                PreparedStatement ps = DbHandler.getConnection().prepareStatement("select count(*)from login where username =? ");
                ps.setString(1,txtuname.getText());
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getInt(1)==1){


                    ps = DbHandler.getConnection().prepareStatement("select theme from theme where username = ?");
                    ps.setString(1,txtuname.getText());
                    rs = ps.executeQuery();
                    if (rs.next()){
                        theme = rs.getString("theme");
                        System.out.println(theme);
                    }


                    // tlogin checks admin/user login

                    if (tlogin) {

                        ps = DbHandler.getConnection().prepareStatement("select count(*)from login where username =? and password= ? and role = 'user'");
                        ps.setString(1, txtuname.getText());
                        ps.setString(2, SHAExample.get_SHA_256_SecurePassword(txtpassword.getText(), "[B@6e2c634b3385185f8ac69287b02c7cc9e6c5385a4706c1fb7e5aaf44d38a33f2cd5bc193"));
                        rs = ps.executeQuery();
                        while (rs.next()){
                            if (rs.getInt(1) == 1) {

                                new MyAlert().createAlert("Welcome " + txtuname.getText() + " to MLT System ", "Info", event);
                                FXMLLoader loader;
                                loader = new CreateStage().createStage("Welcome To System " + txtuname.getText(), "/mlt/ui/Dashboard/teacher/DashoardTeacher.fxml", null, 0, false, true, false, true,theme);
                                DashboardTeacherController controller = loader.getController();
                                CloseStage.closeStage(event);
                                controller.setUsername(txtuname.getText());
                            } else
                                new MyAlert().createAlert("Wrong Credential", "Error", event);
                        }
                    }
                    else
                    {
                            ps = DbHandler.getConnection().prepareStatement("select count(*)from login where username =? and password= ? and role= 'admin'");
                            ps.setString(1,txtuname.getText());
                            ps.setString(2, SHAExample.get_SHA_256_SecurePassword(txtpassword.getText(), "[B@6e2c634b3385185f8ac69287b02c7cc9e6c5385a4706c1fb7e5aaf44d38a33f2cd5bc193"));
                            rs = ps.executeQuery();
                            rs.next();

                            if (rs.getInt(1)==1){
                                new MyAlert().createAlert("Welcome "+ txtuname.getText()+" to MLT System ","Info",event);
                                FXMLLoader loader;
                                loader = new CreateStage().createStage("Welcome To System "+ txtuname.getText(), "/mlt/ui/Dashboard/Dashoard.fxml",null,0,false,true,false,true,theme);

                                CloseStage.closeStage(event);
                                Controller controller = loader.getController();
                                controller.setUsername(txtuname.getText());
                                CloseStage.closeStage(event);
                            }
                            else
                                new MyAlert().createAlert("Wrong Credential", "Error", event);
                    }
                }
                else
                {
                    new MyAlert().createAlert("Please enter correct Username ... "," Login Alert",event);
                    txtuname.getStyleClass().add("wrong-credential");
                }
            }
            else
            {
                new MyAlert().createAlert("Please enter login credential ... "," Login Alert",event);
                txtuname.getStyleClass().add("wrong-credential");
                txtpassword.getStyleClass().add("wrong-credential");
            }
        } catch (SQLException e) {
            new MyAlert().createAlert("Server Error"," Login Alert",event);

            if (e.getLocalizedMessage().contains("Communications link failure"))
                paneerror.toFront();
            System.out.println(e.getLocalizedMessage());
        }

        btnlogin.setDisable(false);
    }

    /*  Toggle Between Themes   */


    @FXML
    void changeWhiteTheme(ActionEvent event) {
        Node n = (Node) event.getSource();
        if (tglblacktheme.isSelected())
            n.getScene().getStylesheets().setAll("mlt/ui/style/newStyle.css","mlt/ui/style/common.css","mlt/ui/style/style.css");
        else
            n.getScene().getStylesheets().setAll("mlt/ui/style/new2.css");
    }


    /* To set theme */

    public void setTheme(String theme){
        this.theme = theme;

        Scene scene = txtuname.getScene();
        if (theme.equals("black"))
        {
            scene.getStylesheets().setAll("mlt/ui/style/newStyle.css","mlt/ui/style/common.css","mlt/ui/style/style.css");
            tglblacktheme.setSelected(true);
        }
        else
        {
            scene.getStylesheets().setAll("mlt/ui/style/new2.css");
            tglblacktheme.setSelected(false);
        }
    }

    /* to make password visible */

    @FXML
    void showpassword(ActionEvent event) {
        if (!showpass)
        {
            showpass = true;
            btnshowpassword.setVisible(false);
            btnshowpassword1.setVisible(true);
            lblshowpassword.setVisible(true);
        }
        else
        {
            lblshowpassword.setVisible(false);
            btnshowpassword1.setVisible(false);
            btnshowpassword.setVisible(true);
            showpass = false;
        }
    }

    /* to go to admin Login */

    @FXML
    void adminLogin(ActionEvent event) {
        glyusers.setVisible(false);
        btnadminlogin.setVisible(false);
        glyuser.setVisible(true);
        btnteacherlogin.setVisible(true);
        GetStage.getStage(event).setTitle("Admin Login");
        tlogin = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        txtpassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                lblshowpassword.setText(newValue);
            }
        });
        lblshowpassword.setText(txtpassword.getText());

//        txtuname.setText("admin");
//        txtpassword.setText("admin");


        try{

            Connection con = DbHandler.getConnection();
            panelogin.toFront();

        } catch (SQLException e) {
            paneerror.toFront();
        }

        sliderAnimation();
    }

    @FXML
    void teacherlogin(ActionEvent event) {
        glyusers.setVisible(true);
        btnadminlogin.setVisible(true);
        glyuser.setVisible(false);
        btnteacherlogin.setVisible(false);
        GetStage.getStage(event).setTitle("User Login");
        tlogin = true;
    }



//    try to connect to Server

    @FXML
    void retry(ActionEvent event) {

        try{
            DbHandler.createLoginTable();
            panelogin.toFront();

        } catch (SQLException e) {
            new MyAlert().createAlert("Can't connect to Server"," Login Alert",event);
            e.getLocalizedMessage();
            paneerror.toFront();
        }

    }


    @FXML
    void closeDbPanel(ActionEvent event) {
        paneerror.setEffect(null);
        paneerror.setDisable(false);
        new FadeOut(panedbconnect).play();
        panedbconnect.toBack();
        new FadeOut(paneremotedbconnect).play();
        paneremotedbconnect.toBack();
    }


    @FXML
    void connect(ActionEvent event) {
       if (!txtdbuname.getText().equals("")){
           try{

               DbHandler.getConnection(txtdbuname.getText(),txtdbpass.getText());
               panelogin.toFront();
               panedbconnect.toBack();

           } catch (SQLException e) {
               new MyAlert().createAlert("Can't connect to Server"," Login Alert",event);
               System.out.println(e.getLocalizedMessage());
//               paneerror.toFront();
           }
       }
       else
           new MyAlert().createAlert("Please Enter Username","Error",event);
    }

    @FXML
    void connectToServer(ActionEvent event) {
        paneerror.setDisable(true);
        paneerror.setEffect(new BoxBlur(2,2,1));
        panedbconnect.toFront();
        new FadeIn(panedbconnect).play();
    }


//    For connecting to remote Db

    @FXML
    void connectRemoteDb(ActionEvent event) {

        if (!(txtrdbuname.getText().equals("") || txtdbconString.getText().equals(""))){
            try{

                DbHandler.getConnection(txtrdbuname.getText(),txtrdbpass.getText(),txtdbconString.getText());
                panelogin.toFront();
                paneremotedbconnect.toBack();

            } catch (SQLException e) {
                new MyAlert().createAlert("Can't connect to Server"," Login Alert",event);
                System.out.println(e.getLocalizedMessage());
//               paneerror.toFront();
            }
        }
        else
            new MyAlert().createAlert("Please Enter Db Credential","Error",event);
    }

    @FXML
    void connectToRemoteServer(ActionEvent event) {
        paneerror.setDisable(true);
        paneerror.setEffect(new BoxBlur(2,2,1));
        paneremotedbconnect.toFront();
        new FadeIn(paneremotedbconnect).play();
    }

    @FXML
    void appexit(ActionEvent event){
        System.exit(0);
    }
    
    private void sliderAnimation(){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3),pane4);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
    
        fadeTransition.setOnFinished(event -> {
            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(3),pane3);
            fadeTransition1.setFromValue(1);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();
    
            fadeTransition1.setOnFinished(event1 -> {
                FadeTransition fadeTransition2 = new FadeTransition(Duration.seconds(3),pane2);
                fadeTransition2.setFromValue(1);
                fadeTransition2.setToValue(0);
                fadeTransition2.play();
    
                fadeTransition2.setOnFinished(event2 -> {
                    FadeTransition fadeTransition0 = new FadeTransition(Duration.seconds(3),pane2);
                    fadeTransition0.setFromValue(1);
                    fadeTransition0.setToValue(8);
                    fadeTransition0.play();
                    
                    fadeTransition0.setOnFinished(event3 -> {
                        FadeTransition fadeTransition11 = new FadeTransition(Duration.seconds(3),pane3);
                        fadeTransition11.setFromValue(1);
                        fadeTransition11.setToValue(0);
                        fadeTransition11.play();

                        fadeTransition11.setOnFinished(event4 -> {
                            FadeTransition fadeTransition22 = new FadeTransition(Duration.seconds(3),pane4);
                            fadeTransition22.setFromValue(1);
                            fadeTransition22.setToValue(0);
                            fadeTransition22.play();

                            fadeTransition22.setOnFinished(event5 -> {
                                sliderAnimation();
                            });
                        });
                    });
                });
            });
        });
    }
}

