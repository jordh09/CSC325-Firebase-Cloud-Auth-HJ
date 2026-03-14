package com.example.csc325_firebase_webview_auth.view;//package modelview;

import com.example.csc325_firebase_webview_auth.model.Person;
import com.example.csc325_firebase_webview_auth.viewmodel.AccessDataViewModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

//import com.google.storage.v2.Bucket;
//import com.google.storage.v2.StorageClient;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;


public class AccessFBView {


    @FXML
    private TextField nameField;
    @FXML
    private TextField majorField;
    @FXML
    private TextField ageField;



    //menubar implement
    @FXML
    private MenuItem registerFile;
    @FXML
    private MenuItem signInFile;
    @FXML
    private MenuItem closeFile;

    @FXML
    private MenuItem deleteEdit;

    @FXML
    private MenuItem aboutHelp;


    //tableview implement
    @FXML
    private TableView<Person> infoTable;
    @FXML
    private TableColumn<Person, String> nameCol;
    @FXML
    private TableColumn<Person, String> majorCol;
    @FXML
    private TableColumn<Person, String> ageCol;


    //profile button
    @FXML
    private ImageView profileImage;

    @FXML
    private Button uploadImage;



    @FXML
    private Button writeButton;
    @FXML
    private Button readButton;
    @FXML
    private TextArea outputField;
     private boolean key;
    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();
    private Person person;
    public ObservableList<Person> getListOfUsers() {
        return listOfUsers;
    }

    void initialize() {

        AccessDataViewModel accessDataViewModel = new AccessDataViewModel();
        nameField.textProperty().bindBidirectional(accessDataViewModel.userNameProperty());
        majorField.textProperty().bindBidirectional(accessDataViewModel.userMajorProperty());
        writeButton.disableProperty().bind(accessDataViewModel.isWritePossibleProperty().not());


    }



    @FXML
    private void addRecord(ActionEvent event) {
        addData();
    }

        @FXML
    private void readRecord(ActionEvent event) {
        readFirebase();
    }


    //menu
    @FXML
    private void regRecord(ActionEvent event) throws IOException {
        registerUser();
        App.setRoot("/files/Registration.fxml");
    }

    @FXML
    private void signInRecord(ActionEvent event) throws IOException {
        App.setRoot("/files/SignIn.fxml");
    }

    @FXML
    private void closeApp() {
        Platform.exit();
    }

    @FXML
    private void deleteMenu(ActionEvent event){
    deleteData();
    }

    @FXML
    private void aboutMenu(){
        System.out.println("CSC 325 Firebase cloud assignment.");
    }

    @FXML
    private void uploadImage(ActionEvent event){
        imageUpload();
    }


     @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("/files/WebContainer.fxml");
    }



   public void imageUpload(){
       FileChooser fileChooser = new FileChooser();
       fileChooser.setTitle("Choose Profile Picture");
       fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

       File selectFile = fileChooser.showOpenDialog(uploadImage.getScene().getWindow());

       if (selectFile != null){
           try{
               Image image = new Image(selectFile.toURI().toString());
               if(profileImage != null){
                   profileImage.setImage(image);
               }
               InputStream fileStream = new FileInputStream(selectFile);

               //Bucket bucket = StorageClient.getInstance().bucket();
               String imageName = "images/"+image;
               //bucket.create(imageName, fileStream, selectFile.getName());

               System.out.println("Profile Image uploaded successfully.");
           }

           catch (Exception e){
               System.err.println("Error uploading image: "+e.getMessage());
           }
       }
   }


    public void deleteData() {
        Person delete = infoTable.getSelectionModel().getSelectedItem();

        if(delete != null) {
            try {
                DocumentReference docRef = App.fstore.collection("References").document(delete.getDocumentID());
                ApiFuture<WriteResult> result = docRef.delete();

                System.out.println("Deletion finished...");
            }
            catch (Exception e){
                System.err.println("Error deleting: "+e.getMessage());
            }
        }
}


    public void addData() {

        DocumentReference docRef = App.fstore.collection("References").document(UUID.randomUUID().toString());

        Map<String, Object> data = new HashMap<>();
        data.put("Name", nameField.getText());
        data.put("Major", majorField.getText());
        data.put("Age", Integer.parseInt(ageField.getText()));
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
    }

        public boolean readFirebase()
         {
             key = false;
        //asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future =  App.fstore.collection("References").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0)
            {
                System.out.println("Outing....");

                for (QueryDocumentSnapshot document : documents)
                {
                    outputField.setText(outputField.getText()+ document.getData().get("Name")+ " , Major: "+
                            document.getData().get("Major")+ " , Age: "+
                            document.getData().get("Age")+ " \n ");
                    System.out.println(document.getId() + " => " + document.getData().get("Name"));


                    person  = new Person(String.valueOf(document.getData().get("Name")),
                            document.getData().get("Major").toString(),
                            Integer.parseInt(document.getData().get("Age").toString()));

                    listOfUsers.add(person);

                    //tableview implement
                    infoTable.setItems(listOfUsers);

                    nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
                    majorCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMajor()));
                    ageCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAge())));

                    person.setDocumentID(document.getId());
                }
            }
            else
            {
               System.out.println("No data");
            }
            key=true;

        }
        catch (InterruptedException | ExecutionException ex)
        {
             ex.printStackTrace();
        }
        return key;
    }

        public void sendVerificationEmail() {
        try {
            UserRecord user = App.fauth.getUser("name");
            //String url = user.getPassword();

        } catch (Exception e) {
        }
    }

    public boolean registerUser() {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail("user@example.com")
                .setEmailVerified(false)
                .setPassword("secretPassword")
                .setPhoneNumber("+11234567890")
                .setDisplayName("John Doe")
                .setDisabled(false);

        UserRecord userRecord;
        try {
            userRecord = App.fauth.createUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());
            return true;

        } catch (FirebaseAuthException ex) {
           // Logger.getLogger(FirestoreContext.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
}
