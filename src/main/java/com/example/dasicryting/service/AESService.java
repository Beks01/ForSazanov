package com.example.dasicryting.service;

import lombok.AllArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;


@Service
public class AESService {

    private static SecretKeySpec secretKey;
    private static byte[] key;


    public static void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            //System.out.println(key);
            sha = MessageDigest.getInstance("SHA-1");
            // System.out.println(sha);
            key = sha.digest(key);
            // System.out.println(key);
            key = Arrays.copyOf(key, 16);
            //System.out.println(key);
            secretKey = new SecretKeySpec(key, "AES");
            //System.out.println(secretKey);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public String decrypt(String strToDecrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }


    public String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            System.out.println(Cipher.ENCRYPT_MODE);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }


    public void downloadEncrypted(String filename, String secret) {
        File file = new File(filename);
        try (Reader reader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = bufferedReader.readLine();
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("August");

            int num = 0;
            while (line != null) {


                XSSFRow row = sheet.createRow(num);

                row.createCell(0).setCellValue(encrypt(line, secret));

                num++;
                line = bufferedReader.readLine();
            }
            FileOutputStream fileOut = new FileOutputStream("doc" +System.currentTimeMillis()+".xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public void downloadDecrypted(String fileName) {
        File file = new File(fileName);
        String secret = "Beks";
        try (Reader reader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line = bufferedReader.readLine();
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("August");

            int num = 0;
            while (line != null) {


                XSSFRow row = sheet.createRow(num);

                row.createCell(0).setCellValue(decrypt(line, secret));

                num++;
                line = bufferedReader.readLine();
            }
            FileOutputStream fileOut = new FileOutputStream("doc" +System.currentTimeMillis()+".xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }


}
