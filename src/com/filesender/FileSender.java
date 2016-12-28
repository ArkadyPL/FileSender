package com.filesender;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

 class SendFile{
    static ServerSocket receiver = null;
    static OutputStream out = null;
    static Socket socket = null;
    static File myFile = new File("C:\\Users\\hieptq\\Desktop\\AtomSetup.exe");
    /*static int count;*/
    static byte[] buffer = new byte[(int) myFile.length()];
    public static void work() throws IOException{
        receiver = new ServerSocket(9099);
        socket = receiver.accept();
        System.out.println("Accepted connection from : " + socket);
        FileInputStream fis = new FileInputStream(myFile);
        BufferedInputStream in = new BufferedInputStream(fis);
        in.read(buffer,0,buffer.length);
        out = socket.getOutputStream();
        System.out.println("Sending files");
        out.write(buffer,0, buffer.length);
        out.flush();
        /*while ((count = in.read(buffer)) > 0){
            out.write(buffer,0,count);
            out.flush();
        }*/
        out.close();
        in.close();
        socket.close();
        System.out.println("Finished sending");



    }

}

 class ReceiveFile{
    static Socket socket = null;
    static int maxsize = 999999999;
    static int byteread;
    static int current = 0;
    public static void work() throws FileNotFoundException, IOException{
        byte[] buffer = new byte[maxsize];
        Socket socket = new Socket("localhost", 9099);
        InputStream is = socket.getInputStream();
        File test = new File("D:\\AtomSetup.exe");
        test.createNewFile();
        FileOutputStream fos = new FileOutputStream(test);
        BufferedOutputStream out = new BufferedOutputStream(fos);
        byteread = is.read(buffer, 0, buffer.length);
        current = byteread;
        while ((byteread = is.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, byteread);
        }

        out.flush();
        socket.close();
        fos.close();
        is.close();

    }
}

public class FileSender {
    public static void main(String[] args) {
        //GETTING IP
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String ip = null; //you get the IP as a String
        try {
            ip = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(ip);
        //END OF GETTING IP

        //SETTING UP GUI WITH FILE PATHS TREE
        JFrame frame = new JFrame("File Sender");
        //tree
        // Figure out where in the filesystem to start displaying
        File root;
        if (args.length > 0) root = new File(args[0]);
        else root = new File(System.getProperty("user.home"));

        // Create a TreeModel object to represent our tree of files
        FileTreeModel model = new FileTreeModel(root);
        Object ptr = null;
        // Create a JTree and tell it to display our model
        JTree tree = new JTree();
        tree.setModel(model);
        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(1,2));
        // The JTree can get big, so allow it to scroll.
        JScrollPane scrollpane = new JScrollPane(tree);

        // Display it all in a window and make the window appear
        //frame.add(scrollpane, "Center");
        frame.setSize( 1000, 600); // Set frame size
        frame.setLocationRelativeTo(null); // Put frame in center of the screen
        JLabel IPlabel = new JLabel("       Hello! Your IP is : " + ip);
        panel.add(scrollpane);
        panel.add(IPlabel);

        frame.add(panel);
        frame.setVisible(true);

        //LISTENER GETS CURRENTLY POINTED DIRECTORY
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                Object object = e.getPath().getLastPathComponent();
                if (object instanceof File){
                    File file = (File) object;
                }
                System.out.println("You selected " + object);
            }
        });
        try {
            SendFile.work();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}