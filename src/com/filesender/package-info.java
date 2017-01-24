/**
 * General package for the project FileSender.
 * <h1>How does FileSender work - general description.</h1>
 * <h2>Start up of the program</h2>
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
 * <h2>When user starts acting</h2>
 * <h2>When remote requests are coming</h2>
 */
package com.filesender;