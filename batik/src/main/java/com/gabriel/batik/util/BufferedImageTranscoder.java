package com.gabriel.batik.util;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import java.awt.image.BufferedImage;

public class BufferedImageTranscoder extends ImageTranscoder {

    private BufferedImage img = null;

    BufferedImageTranscoder(double width, double height) {
        this.width = (float) width;
        this.height = (float) height;
    }

    BufferedImageTranscoder(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public BufferedImage createImage(int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return bi;
    }

    @Override
    public void writeImage(BufferedImage img, TranscoderOutput to) throws TranscoderException {
        this.img = img;
    }

    public BufferedImage getBufferedImage() {
        return img;
    }
}
