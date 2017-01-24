/**
 * <p>Main package for the project FileSender.</p>
 * <h1>How does FileSender work - general description.</h1>
 * <h2>1. Start up of the program</h2>
 * <p>On start up, {@link com.filesender.FileSender#main(java.lang.String[])} function is called.
 * The function opens the new server socket that is meant to accept incoming 'ping' connections,
 * so the others know that the application is up. After it initializes {@link com.filesender.HelperClasses.ServerStatus}
 * object which will tell us if remote that we connected to is still available. After, app initializes
 * {@link com.filesender.Cryptography.RSA} and {@link com.filesender.Cryptography.AES} classes due to the need for
 * future encryption/decryption. After it checks local ip address. Then it creates whole the GUI, including the
 * {@link com.filesender.GuiElements.Toolbar} and JTree panes for {@link com.filesender.FileTreeModel} to fill with
 * the data. After it turns on some listeners to enable files trees' servicing. In the very end, there is a call
 * of {@link com.filesender.Connection#ListenForIncomingConnections}, a function that will listen for and handle
 * all incoming requests.</p>
 * <h2>2. When user starts acting</h2>
 * <h3>2.1. When not connected</h3>
 * <p>If application is not connected to any server, possibilities for user are limited to connecting to someone.
 * One can do so by delivering the proper data to the form on the right side of the toolbar and confirming by mouse
 * click on the 'Connection' button or by pressing enter. In the form, 'tab' button works when one wants to switch
 * between the fields. After accepting data is validated and if it is proper, connection is set up.</p>
 * <h3>2.2. When connected</h3>
 * <p>If we as a user are connected to the remote server, we can request and send files to them. We can also move
 * between different levels of their and our files trees. To request file to be sent it is enough to double click it.
 * All the data (including files, files trees, etc.) is encrypted using AES encryption before being sent.
 * When we are done with our job we can disconnect from the server using 'Disconnect' button and then connected to
 * someone else.</p>
 * <h2>3. When remote requests are coming</h2>
 * <p>Special method was designed for receiving and handling all the incoming remote requests
 * ({@link com.filesender.Connection#ListenForIncomingConnections}). It is called in the end of main function of program
 * in the infinite loop while(true). Connections are accepted only if we are not connected to any server or if the connection
 * is coming from this server. Request message always consists of object of type {@link com.filesender.HelperClasses.Operation},
 * which contains all the necessary information. Depending on operation id we can be requested to send a file, a files tree,
 * set up the new connection or to do some other similar things.</p>
 * <h2>4. Additional information</h2>
 * <p>1. FileSender application can act as both client and server at once but can be connected only to one remote device at one time.</p>
 * <p>2. All connections are 'virtual' in a sense that for each new request, new connection socket is created
 * and after request is realized socked is closed.</p>
 */
package com.filesender;