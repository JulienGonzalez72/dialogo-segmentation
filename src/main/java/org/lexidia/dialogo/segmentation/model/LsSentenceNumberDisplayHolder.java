/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lexidia.dialogo.segmentation.model;

import fr.lexiphone.player.impl.SentenceNumber;

/**
 *
 * @author Haerwynn
 */
public class LsSentenceNumberDisplayHolder {

    private static LsSentenceNumberDisplayHolder me;
    private SentenceNumber sentenceNumber;

    private LsSentenceNumberDisplayHolder() {
        sentenceNumber=null;
    }

    public boolean isOn() {
        return sentenceNumber!=null;
    }

    public SentenceNumber getSentenceNumber() {
        return sentenceNumber;
    }

    public void setSentenceNumber(SentenceNumber sentenceNumber) {
        this.sentenceNumber = sentenceNumber;
    }

    public static LsSentenceNumberDisplayHolder getInstance() {
        if (me == null) {
            me = new LsSentenceNumberDisplayHolder();
        }
        return me;
    }

}
