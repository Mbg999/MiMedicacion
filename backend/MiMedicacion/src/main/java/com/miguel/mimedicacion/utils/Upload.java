/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.miguel.mimedicacion.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Path;

/**
 *
 * @author miguel
 */
public class Upload {

    private static final String VALID_PICTURE_EXTENSIONS[] = {".png", ".jpg"};
    // don't forget to create these folders before using this app
    private static final String FILES_PATH = "C:" + File.separator + "MiMedicacionAssets" + File.separator;
    private static final String USER_PICTURES_PATH = "users" + File.separator + "pictures" + File.separator;

    /**
     * Upload a user picture
     * 
     * @param file InputStream
     * @param ext String
     * @return Map&lt;String, String&gt; error, picture (the name to store into the db)
     */
    public static Map<String, String> userPicture(InputStream file, String ext) {
        Map<String, String> result = new HashMap();
        OutputStream out = null;
        String pictureName;

        pictureName = new Date().getTime() + ext; // new generated name
        try {
            int read;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(Upload.FILES_PATH + Upload.USER_PICTURES_PATH + pictureName));
            while ((read = file.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            result.put("picture", pictureName);
        } catch (IOException ex) {
            result.put("error", "Uploading error, try again");
            Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return result;
    }

    /**
     * Delete a user picture file
     * 
     * @param picture String
     * @return boolean
     */
    public static boolean deleteUserPicture(String picture) {
        Path picturePath = Paths.get(Upload.FILES_PATH + Upload.USER_PICTURES_PATH + picture);
        boolean deleted = false;
        try {
            Files.delete(picturePath);
            deleted = true;
        } catch (IOException e) {
        }

        return deleted;
    }

    /**
     * Validates if a extension is in the valid picture extensions
     * 
     * @param ext String
     * @return boolean
     */
    public static boolean isAValidPictureExtension(String ext) {
        return isValidExtension(ext, Upload.VALID_PICTURE_EXTENSIONS);
    }

    /**
     * Search if a extension is in a valid extensions array
     * 
     * @param ext String
     * @param validExtensions String[]
     * @return boolean
     */
    private static boolean isValidExtension(String ext, String validExtensions[]) {
        boolean valid = false;

        for (String extension : validExtensions) {
            if (extension.equalsIgnoreCase(ext)) {
                valid = true;
            }
        }
        return valid;
    }
}
