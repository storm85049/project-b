package controller.logger;

import Krypto.ElGamal;
import Krypto.IAsymmetricEncryption;
import client.ClientData;
import client.RemoteClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.json.simple.JSONObject;
import sun.tools.jar.CommandLine;
import util.Actions;
import util.ChatViewUtil;
import util.ModeMapper;

import javax.jws.WebParam;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Logger {




    private VBox log = null;
    private Text text;
    private TextFlow textFlow;

    private static Logger instance;

    private ArrayList<HBox> queue;



    static final  String TAB ="\t";
    static final  String LB ="\n";
    private String referrer;
    private String lastEncryptedMessage;
    private String lastDecryptedMessage;
    private String lastSymMode;
    private String lastKey;


    public static  Logger getInstance() {
        if (Logger.instance == null){
            Logger.instance = new Logger();
        }
        return Logger.instance;
    }

    private Logger()
    {
        this.queue = new ArrayList<>();

    }


    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public void log(String type, String message)
    {
        this.log = (VBox) ChatViewUtil.find("logPane");

        Platform.runLater(()->{

            HBox box = new HBox();
            switch (type){
                case(Actions.LOG_INIT_CHAT):
                    box = this.logInitChat(message);
                    break;
                case(Actions.LOG_OFFLINE):
                    box = this.logOffline(true,message);
                    break;
                case(Actions.LOG_ONLINE):
                    box = this.logOffline(false,message);
                    break;
                case(Actions.LOG_REMOTE_MESSAGE):
                    box = this.logRemoteMessage(true,message);
                    break;
                case(Actions.LOG_SELF_MESSAGE):
                    box = this.logRemoteMessage(false,message);
                    break;
            }

            if(this.log == null){
                this.queue.add(box);
            }
            else{
                this.log.setAlignment(Pos.TOP_LEFT);
                for (HBox boxInQueue : this.queue) {
                    this.log.getChildren().add(boxInQueue);
                }
                this.log.getChildren().add(box);
                this.queue = new ArrayList<>();
            }
        });
    }

    public void resolveQueue()
    {
        if(this.log !=null){
            this.log.setAlignment(Pos.TOP_LEFT);
            for (HBox box : this.queue) {
                this.log.getChildren().add(box);
            }
        }
    }

    private HBox logRemoteMessage(boolean remote, String name)
    {

        Text text;
        if(remote){
            text=new Text(timestamp() + name + " sent a message!"+ LB);
        }else{
            text=new Text(timestamp() + "you sent "+name+" a message!"+ LB);

        }
        Text text1 =new Text(TAB + "symetric mode: " + ModeMapper.map(this.getLastSymMode())+LB);
        Text text2 =new Text(TAB + "symetric key: " + this.getLastKey()+LB);
        Text text3 =new Text(TAB + "encrypted: " + this.getLastEncryptedMessage()+LB);
        Text text4 =new Text(TAB + "decrypted: " + this.getLastDecryptedMessage()+LB);
        text = setStyle(text,Color.WHITE);
        text1 = setStyle(text1,Color.GRAY);
        text2 = setStyle(text2,Color.GRAY);
        text3 = setStyle(text3,Color.WHITE);
        text4 = setStyle(text4,Color.WHITE);

        TextFlow tempFlow = new TextFlow();
        tempFlow.getChildren().addAll(text,text1,text2,text3,text4);
        TextFlow flow=new TextFlow(tempFlow);
        HBox hbox=new HBox(12);


        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().add(flow);
        hbox.prefWidthProperty().bind(this.log.widthProperty());
        return hbox;
    }


    private HBox logOffline(boolean offline, String name)
    {

        Text text;

        if(offline){
            text=new Text(timestamp() + name + " went offline !"+ LB);
            text = setStyle(text,Color.INDIANRED);
        }else{
            text=new Text(timestamp() + name + " is online!"+ LB);
            text = setStyle(text,Color.GREENYELLOW);
        }



        TextFlow tempFlow = new TextFlow();
        tempFlow.getChildren().addAll(text);
        TextFlow flow=new TextFlow(tempFlow);
        HBox hbox=new HBox(12);


        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().add(flow);
        return hbox;

    }

    private  HBox  logInitChat(String from)
    {

        RemoteClient remoteClient = ClientData.getInstance().getRemoteClientById(from);
        Text text = null;

        if(this.getReferrer().equals(Actions.REFERRER_CREATE)){
             text=new Text(timestamp() + "New Chat initialized with " + remoteClient.getName() + "" + LB);
        }
        else if(this.getReferrer().equals(Actions.REFERRER_UPDATE)){
            text=new Text(timestamp() + "Changing keys with " + remoteClient.getName() + "" + LB);
        }
        JSONObject data = remoteClient.getRequestedEncryptionData();


        String asymMode = (String) data.get("asymMode");
        String symMode = (String) data.get("symMode");
        JSONObject encryptionParams = (JSONObject) data.get("encryptionParams");
        String encryptedKey = (String) encryptionParams.get("encryptedKey");
        String key = "";

        if(symMode.equals(Actions.MODE_HILL)){
            key = remoteClient.getSymEncryption().getModeSpecificKey();

        }else{
            key = (String)encryptionParams.get("key");
        }

        if(key == null){
            key = remoteClient.getSymEncryption().getModeSpecificKey();
        }


        IAsymmetricEncryption enc = null;
        switch (asymMode){
            case(Actions.MODE_ELGAMAL):
                enc = ClientData.getInstance().getElGamal();
                break;
            case(Actions.MODE_RSA):
                enc = ClientData.getInstance().getRSA();
                break;
                default:break;
        }


        text = setStyle(text,Color.WHITE);
        Text text2 =new Text(TAB + ModeMapper.map(symMode) + " key '" + key +"' encrypted with your public " + ModeMapper.map(asymMode) + " key" + LB);
        Text text3 =new Text(TAB + "your public " + ModeMapper.map(asymMode) +" key: ");
        Text text7 = new Text(this.readable(70,enc.getPublicKey())+ LB);

        Text text4 =new Text(TAB + "resulting encrypted key: " );
        Text text8 =new Text( this.readable(30,encryptedKey) + LB);

        Text text5 =new Text(TAB + "decrypted with your private " + ModeMapper.map(asymMode) + " key: ");
        Text text9 =new Text(this.readable(70,enc.getPrivateKey() )+ LB);

        Text text6 =new Text(TAB + TAB + "results in decrypted key '" + key +"'" + LB);
        Text text10 =new Text(TAB  + "both of you can encrypt messages symmetrically now with the key '" + key +"'  " + LB + TAB + "without anyone else possibly knowing this key");
        text2 = setStyle(text2,Color.WHITE);;
        text3 = setStyle(text3,Color.WHITE);
        text4 = setStyle(text4,Color.WHITE);
        text5 = setStyle(text5,Color.WHITE);
        text6 = setStyle(text6,Color.WHITE);
        text7 = setStyle(text7, Color.YELLOW);
        text8 = setStyle(text8, Color.YELLOW);
        text9 = setStyle(text9, Color.YELLOW);
        text10 = setStyle(text10, Color.GREEN);

        TextFlow tempFlow = new TextFlow();
        tempFlow.getChildren().addAll(text,text2,text3,text7,text4,text8,text5,text9,text6, text10);
        TextFlow flow=new TextFlow(tempFlow);
        HBox hbox=new HBox(12);


        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().add(flow);


        return hbox;


    }

    private Text setStyle(Text text, Color color){

        text.setFont(Font.font("Lucida Sans Typewriter",12));
        text.setFill(color);
        return text;

    }


private String readable(int offset, String dirtyString)
{


    String tmp =LB + TAB;
    for (int i = 0; i< dirtyString.length();i++){
        tmp += dirtyString.charAt(i);
        if((i+1) % offset == 0){
            tmp += LB + TAB;
        }
    }

    return tmp;

}

private String timestamp()
{
    Date date = new Date();
    String strDateFormat = "hh:mm:ss a";
    DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
    return dateFormat.format(date) + ": ";
}


    public String getLastEncryptedMessage() {
        return lastEncryptedMessage;
    }

    public void setLastEncryptedMessage(String lastEncryptedMessage) {
        this.lastEncryptedMessage = lastEncryptedMessage;
    }

    public String getLastDecryptedMessage() {
        return lastDecryptedMessage;
    }

    public void setLastDecryptedMessage(String lastDecryptedMessage) {
        this.lastDecryptedMessage = lastDecryptedMessage;
    }

    public String getLastSymMode() {
        return lastSymMode;
    }

    public void setLastSymMode(String lastSymMode) {
        this.lastSymMode = lastSymMode;
    }

    public String getLastKey() {
        return lastKey;
    }

    public void setLastKey(String lastKey) {
        this.lastKey = lastKey;
    }


    public void clearLog()
    {
        if(this.log != null){
            VBox logOnGUI = (VBox) ChatViewUtil.find("logPane");
            logOnGUI.getChildren().clear();
        }
    }


}
