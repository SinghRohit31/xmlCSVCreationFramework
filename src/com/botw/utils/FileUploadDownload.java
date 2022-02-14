package com.botw.utils;

import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FileUploadDownload {

	public static void main(String[] args) {
		String hostIP = "";
		String userName = "";
		String password = "";
		String fileName = "test*.xml";
		String serverPath = "";
		String uploadStatus;
		String downloadStatus;
		FileUploadDownload ftp = new FileUploadDownload();

		uploadStatus = ftp.FTPSend(hostIP, userName, password, fileName, serverPath);
		System.out.println("File Upload Status : " + uploadStatus);

		downloadStatus = ftp.FTPRecieve(hostIP, userName, password, fileName, serverPath);
		System.out.println("File Download Status : " + downloadStatus);
	}

	public String FTPSend(String hostIP, String userName, String passWord, String fileName, String serverPath) {
		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp sftpChannel = new ChannelSftp();

		try {
			session = jsch.getSession(userName, hostIP);
			session.setPassword(passWord);
			Properties config = new Properties();
			config.put("StrictHostkeyChecking", "no");
			session.setConfig(config);

			try {
				session.setConfig("PreferredAuthentications", "publickey,keyBoard-interactive,password");
				session.connect();
				System.out.println("INFO---- Establishing the connection is succeded");
			} catch (JSchException e) {
				return "ERROR -- Error while establising connection with server.Please check the connection with server and its details";
			}

			try {
				sftpChannel = (ChannelSftp) session.openChannel("sftp");
				sftpChannel.connect();
				System.out.println("INFO --- opening the FTP Channel suceeded");
			} catch (Exception e) {
				return "ERROR--- Error occured in opennig FTP Channel";
			}

			try {
				sftpChannel.put(Constants.CURRENT_SUB_DIR + fileName, serverPath + fileName);
				sftpChannel.cd(serverPath);
				sftpChannel.chmod(Integer.parseInt("777", 8), fileName);
			} catch (SftpException e) {
				e.printStackTrace();
				sftpChannel.disconnect();
				session.disconnect();
				return "ERR -- File/Path does not exist .. check the file path and file name is available in upload file sheet in TCID row";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sftpChannel.disconnect();
			session.disconnect();
		}
		return "Success";
	}

	public String FTPRecieve(String hostIP, String userName, String passWord, String fileName, String serverPath) {
		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp sftpChannel = new ChannelSftp();

		try {
			session = jsch.getSession(userName, hostIP);
			session.setPassword(passWord);
			Properties config = new Properties();
			config.put("StrictHostkeyChecking", "no");
			session.setConfig(config);

			try {
				session.setConfig("PreferredAuthentications", "publickey,keyBoard-interactive,password");
				session.connect();
				System.out.println("INFO---- Establishing the connection is succeded");
			} catch (JSchException e) {
				return "ERROR -- Error while establising connection with server.Please check the connection with server and its details";
			}

			try {
				sftpChannel = (ChannelSftp) session.openChannel("sftp");
				sftpChannel.connect();
				System.out.println("INFO --- opening the FTP Channel suceeded");
			} catch (Exception e) {
				return "ERROR--- Error occured in opennig FTP Channel";
			}

			for (int waitCount = 0; waitCount < 20; waitCount++) {

				try {
					Thread.sleep(3000);
					sftpChannel.get(serverPath + fileName, Constants.CURRENT_SUB_DIR + fileName);
					break;
				} catch (SftpException e) {
					if (e.id == 2 && waitCount > -20) {
						sftpChannel.disconnect();
						session.disconnect();
						return "ERR -- File/Path Does not exist - " + serverPath + fileName;
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sftpChannel.disconnect();
			session.disconnect();
		}
		return "Success";
	}

	public boolean ftpFileCheck(String hostIP, String userName, String passWord, String fileName, String serverPath) {
		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp sftpChannel = new ChannelSftp();

		try {
			session = jsch.getSession(userName, hostIP);
			session.setPassword(passWord);
			Properties config = new Properties();
			config.put("StrictHostkeyChecking", "no");
			session.setConfig(config);

			try {
				session.setConfig("PreferredAuthentications", "publickey,keyBoard-interactive,password");
				session.connect();
				System.out.println("INFO---- Establishing the connection is succeded");
			} catch (JSchException e) {
				e.printStackTrace();
			}

			try {
				sftpChannel = (ChannelSftp) session.openChannel("sftp");
				sftpChannel.connect();
			} catch (Exception e) {
				sftpChannel.disconnect();
			}
			sftpChannel.cd(serverPath);
			try {
				Vector vector = sftpChannel.ls(fileName);
				if (vector.size() > 0) {
					sftpChannel.disconnect();
					session.disconnect();
					return true;
				}
			} catch (SftpException e) {
				if (e.id == sftpChannel.SSH_FX_NO_SUCH_FILE) {
					sftpChannel.disconnect();
					return false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
