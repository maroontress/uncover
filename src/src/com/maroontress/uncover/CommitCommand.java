package com.maroontress.uncover;

import com.maroontress.cui.OptionListener;
import com.maroontress.cui.Options;
import com.maroontress.cui.OptionsParsingException;
import com.maroontress.uncover.coverture.Parser;
import com.maroontress.uncover.coverture.ParsingException;
import java.util.Calendar;

/**
   commit���ޥ�ɤǤ���
*/
public final class CommitCommand extends Command {
    /** ���ޥ��̾�Ǥ��� */
    public static final String NAME = "commit";

    /** ���ޥ�ɤΰ����������Ǥ��� */
    public static final String ARGS = "FILE";

    /** ���ޥ�ɤ������Ǥ��� */
    public static final String DESC = "Commits FILE into a data base.";

    /** �ץ���������̾�Ǥ��� */
    private String projectName;

    /** ��ӥ����Ǥ��� */
    private String revision;

    /** �����ॹ����פǤ��� */
    private String timestamp;

    /** �ץ�åȥե�����Ǥ��� */
    private String platform;

    /** Coverture�����Ϥ����ե�����Υѥ��Ǥ��� */
    private String xmlFile;

    /**
       commit���ޥ�ɤΥ��󥹥��󥹤��������ޤ���

       @param props �ץ��ѥƥ�
       @param av ���ޥ�ɤΰ���������
    */
    public CommitCommand(final Properties props, final String[] av) {
	super(props);
	final Options opt = new Options();

	opt.add("help", new OptionListener() {
	    public void run(final String name, final String arg) {
		usage(opt);
	    }
	}, "Show this message and exit.");

	opt.add("project", new OptionListener() {
	    public void run(final String name, final String arg) {
		projectName = arg;
	    }
	}, "ARG", "Specify a project name.  Required.");

	opt.add("revision", new OptionListener() {
	    public void run(final String name, final String arg) {
		revision = arg;
	    }
	}, "ARG", "Specify a revision.  Required.");

	opt.add("timestamp", new OptionListener() {
	    public void run(final String name, final String arg) {
		timestamp = arg;
	    }
	}, "ARG", "Specify a date.  ARG must be formatted as\n"
		+ "'YYYY-MM-DD hh:mm:ss'.");

	opt.add("platform", new OptionListener() {
	    public void run(final String name, final String arg) {
		platform = arg;
	    }
	}, "ARG", "Specify a platform.");

	String[] args = null;
	try {
	    args = opt.parse(av);
	} catch (OptionsParsingException e) {
	    System.err.println(e.getMessage());
	    usage(opt);
	}
	if (args.length < 1) {
	    System.err.println("FILE must be specified.");
	    usage(opt);
	}
	if (args.length > 1) {
	    System.err.println("too many arguments: " + args[1]);
	    usage(opt);
	}
	if (projectName == null || projectName.isEmpty()) {
	    System.err.println("--project=ARG must be specified.");
	    usage(opt);
	}
	if (revision == null || revision.isEmpty()) {
	    System.err.println("--revision=ARG must be specified.");
	    usage(opt);
	}
	if (timestamp == null) {
	    Calendar cal = Calendar.getInstance();
	    timestamp = String.format("%04d-%02d-%02d %02d:%02d:%02d",
				      cal.get(Calendar.YEAR),
				      cal.get(Calendar.MONTH) + 1,
				      cal.get(Calendar.DAY_OF_MONTH),
				      cal.get(Calendar.HOUR_OF_DAY),
				      cal.get(Calendar.MINUTE),
				      cal.get(Calendar.SECOND));
	}
	if (platform == null) {
	    platform = String.format("%s %s %s",
				     System.getProperty("os.name"),
				     System.getProperty("os.arch"),
				     System.getProperty("os.version"));
	}
	xmlFile = args[0];
    }

    /**
       {@inheritDoc}
    */
    public void run() {
	try {
	    Parser parser = new Parser(xmlFile);
	    String subname = getProperties().getDBFile();
	    DB db = Toolkit.getInstance().createDB(subname);
	    db.commit(projectName, revision, timestamp, platform, parser);
	    db.close();
	} catch (ParsingException e) {
            e.printStackTrace();
	    System.exit(1);
	} catch (DBException e) {
            e.printStackTrace();
	    System.exit(1);
	}
    }
}