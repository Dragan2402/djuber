package com.djuber.djuberbackend.Infastructure.Util;

import com.djuber.djuberbackend.Domain.Authentication.UserType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.Base64;

@Service
public class MediaService {

    @Async
    public void saveBase64AsPicture(Long id, UserType userType, String base64String){
        String[] strings = base64String.split(",");
        String extension = switch (strings[0]) {//check image's extension
            case "data:image/jpeg;base64" -> "jpeg";
            case "data:image/png;base64" -> "png";
            default ->//should write cases for more images types
                    "jpg";
        };
        //convert base64 string to binary data
        byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
        String path = "src/main/resources/pictures/"+userType.toString()+"/id"+id.toString()+"id."+ extension;
        File file = new File(path);
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void setUserDefaultPicture(Long id, UserType userType){
        String inputFilePath ="src/main/resources/pictures/default.png";
        String outputFilePath = "src/main/resources/pictures/"+userType.toString()+"/id"+id.toString()+"id.png";

        try {
            File inputFile = new File(inputFilePath);
            FileOutputStream fos = null;
            fos = new FileOutputStream(outputFilePath);
            FileInputStream fis = new FileInputStream(inputFile);
            byte[] data = new byte[(int) inputFile.length()];
            fis.read(data);
            fis.close();
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteUserPreviousPicture(Long id, UserType userType){

        String folderName="src/main/resources/pictures/"+userType.toString();
        String imageName = "id"+id.toString()+"id";

        File folder = new File(folderName);

        File[] files = folder.listFiles();

        for (File file : files) {
            // If the file name starts with the provided name, delete it
            if (file.getName().startsWith(imageName)) {
                file.delete();
            }
        }

    }

    public String readUserPictureAsBase64String(Long id, UserType userType){
        File directory = new File("src/main/resources/pictures/"+userType.toString()+"/");
        String fileName="id"+id.toString()+"id";
        File[] files = directory.listFiles((dir, name) -> name.startsWith(fileName));
        assert files != null;
        if (files.length == 0) {
            System.out.println("File not found");
        }
        String filePath = files[0].getAbsolutePath();
        //String fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1);
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String base64 = Base64.getEncoder().encodeToString(data);
            return base64;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
