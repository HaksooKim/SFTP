import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * ������ �����Ͽ� ������ ���ε��ϰ�, �ٿ�ε��Ѵ�.
 *
 * @author haneulnoon
 * @since 2009-09-10
 *
 */
public class SFTPUtil{
    private Session session = null;

    private Channel channel = null;

    private ChannelSftp channelSftp = null;
    
    public static void main(String args[]) {

        String host = "sftp�ּ�";
        int port = 8522;
        String userName = "���̵�";
        String password = "��й�ȣ";
        String dir = "/������/"; //������ ������ ��ġ�� ���
        String file = "f:\\test.txt(���ε��ų ����)";

        SFTPUtil util = new SFTPUtil();
        util.init(host, userName, password, port);
        util.upload(dir, new File(file));

		String fileName="�ٿ�ε� ���� ���ϸ�"; 
		String saveDir="������ ��ġ";

        util.download(dir, fileName, saveDir);

        util.disconnection();
        System.exit(0);
    }

    /**
     * ������ ���ῡ �ʿ��� ������ ������ �ʱ�ȭ ��Ŵ
     *
     * @param host
     *            ���� �ּ�
     * @param userName
     *            ���ӿ� ���� ���̵�
     * @param password
     *            ��й�ȣ
     * @param port
     *            ��Ʈ��ȣ
     */
    public void init(String host, String userName, String password, int port) {
        JSch jsch = new JSch();
        try {
            session = jsch.getSession(userName, host, port);
            session.setPassword(password);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }

        channelSftp = (ChannelSftp) channel;

    }

    /**
     * �ϳ��� ������ ���ε� �Ѵ�.
     *
     * @param dir
     *            �����ų �ּ�(����)
     * @param file
     *            ������ ����
     */
    public void upload(String dir, File file) {

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            channelSftp.cd(dir);
            channelSftp.put(in, file.getName());
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * �ϳ��� ������ �ٿ�ε� �Ѵ�.
     *
     * @param dir
     *            ������ ���(����)
     * @param downloadFileName
     *            �ٿ�ε��� ����
     * @param path
     *            ����� ����
     */
    public void download(String dir, String downloadFileName, String path) {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            channelSftp.cd(dir);
            in = channelSftp.get(downloadFileName);
        } catch (SftpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            out = new FileOutputStream(new File(path));
            int i;

            while ((i = in.read()) != -1) {
                out.write(i);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * �������� ������ ���´�.
     */
    public void disconnection() {
        channelSftp.quit();

    }




    


}