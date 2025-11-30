package com.gabriel.draw.service;

import com.gabriel.draw.component.FileTypeFilter;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.model.Drawing;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class ImageFileService {
    public void setImage(Drawing drawing) {
        String homeFolder;
        if(drawing.getImageFilename()==null)

        {
            homeFolder = FileSystemView.getFileSystemView().getHomeDirectory().getPath();
        }
                else

        {
            File file = new File(drawing.getImageFilename());
            homeFolder = file.getPath();
        }

        JFileChooser fileChooser = new JFileChooser(homeFolder);
        FileTypeFilter pngTypeFilter = new FileTypeFilter("png", "PNG Image Documents");
        FileTypeFilter jpgTypeFilter = new FileTypeFilter("jpg", "JPEG Image Documents");
        FileTypeFilter gifTypeFilter = new FileTypeFilter("gif", "GIF Image Documents");
                fileChooser.addChoosableFileFilter(pngTypeFilter);
                fileChooser.addChoosableFileFilter(jpgTypeFilter);
                fileChooser.addChoosableFileFilter(gifTypeFilter);
        int result = fileChooser.showOpenDialog(null);
                if(result ==JFileChooser.APPROVE_OPTION)

        {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            drawing.setImageFilename(filename);
            drawing.setShapeMode(ShapeMode.Image);
        }
    }

}
