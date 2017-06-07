/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.Entrada;

import com.crumbits.ReturnClasses.GcsFile;

/**
 *
 * @author Miquel Ferriol
 */
public class EntradaFile {
    private String file;
    private GcsFile gcsFile;
    private String hola;

    /**
     *
     * @return
     */
    public String getHola() {
        return hola;
    }

    /**
     *
     * @param hola
     */
    public void setHola(String hola) {
        this.hola = hola;
    }

    /**
     *
     * @return
     */
    public String getFile() {
        return file;
    }

    /**
     *
     * @param file
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     *
     * @return
     */
    public GcsFile getGcsFile() {
        return gcsFile;
    }

    /**
     *
     * @param gcsFile
     */
    public void setGcsFile(GcsFile gcsFile) {
        this.gcsFile = gcsFile;
    }
    
    
}
