/**
 * Main package for the project FileSender.
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
 * <p></p>
 * <h2>3. When remote requests are coming</h2>
 * <p></p>
 */
package com.filesender;