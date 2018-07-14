package util;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class TestBase {

    public static HashMap<String, Object> data = new HashMap<String, Object>();
    public HashMap<String, Object> getDataMap = new HashMap<String, Object>();
    public HashMap<String, Object> postDataMap = new HashMap<String, Object>();
    public String[] arrParams =  new String[3];
    public static String env = "qa";

    static {
        PropertyConfigurator.configure("log4j.properties");
    }

    String relativelyPath = System.getProperty("user.dir");
    String testDataPath = relativelyPath + "/test_data/";
    private static final Logger logger = LoggerFactory.getLogger(TestBase.class);


    @AfterSuite(groups = {"smoke", "function","check"})
    public void allAfterSuite() throws Exception {
        String filename = Tool.strTime("_yyyy-MM-dd_HH_mm_ss", 0);
        File src_all = new File("logs/all.html");
        System.out.println(src_all.exists());
        File desc_all = new File("logs/all" + filename + ".html");
        boolean b = src_all.renameTo(desc_all);
        File src_error = new File("logs/error.html");
        File desc_error = new File("logs/error" + filename + ".html");
        src_error.renameTo(desc_error);

    }


    @DataProvider
    public Object[][] testData(Method method) {
        Object[][] data = null;
        Workbook book = null;
        Sheet sheet = null;
        int rowNum = 0;
        int columnNum = 0;
        String clsname = getClass().getName();
        String[] arrName = clsname.split("\\.");
        String project = arrName[arrName.length - 3];
        String model = arrName[arrName.length - 2];
        String interfaceName = arrName[arrName.length - 1];
        String methodname = method.getName();
        try {
            String casename = this.testDataPath + "/" + project + "/" + model + "/" + interfaceName + ".xls";
            book = Workbook.getWorkbook(new File(casename));
            sheet = book.getSheet(methodname);
            rowNum = sheet.getRows();
            columnNum = sheet.getColumns();
            data = new Object[rowNum - 1][];
            int paramSize = method.getParameterTypes().length;
            logger.debug("example 参数=" + paramSize + " excel参数" + sheet.getRow(0).length);
            if (sheet.getRow(0).length != paramSize) {
                logger.error("数据驱动文件与测试脚本参数个数不一致,驱动文件参数个数:" + sheet.getRow(0).length
                        + ";测试脚本参数个数:" + paramSize);
                return null;
            }
            Class[] params = method.getParameterTypes();
            for (int i = 1; i < rowNum; i++) {
                Cell[] rowsCell = sheet.getRow(i);
                Object[] rows = new Object[columnNum];
                for (int j = 0; j < columnNum; j++) {
                    rows[j] = Tool.converter(params[j], rowsCell[j].getContents());
                }
                data[i - 1] = rows;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public void setData(Object... args) {
        StackTraceElement[] temp = Thread.currentThread().getStackTrace();
        StackTraceElement a = temp[2];
        Method[] methods = getClass().getMethods();
        for (Method m : methods) {
            if (m.getName().equals(a.getMethodName())) {
                Parameter[] ps = m.getParameters();
                if (ps.length <= 2) {
                    return;
                }
                for (int i = 2; i < args.length + 2; i++) {
                    if(ps[i].getName().startsWith("arg" + i) ) {
                        logger.error("请配置-parameters.");
                        System.exit(1);
                    }
                    if (ps[i].getType() == (int.class) && ((int) args[i - 2]) == -65535) {
                        continue;
                    }
                    if (ps[i].getType() == String.class && ((String) args[i - 2]) == null) {
                        continue;
                    }
                    if (ps[i].getType() == Boolean.class && ((Boolean) args[i - 2]) == null) {
                        continue;
                    }
                    if (ps[i].getType() == String[].class && ((String[]) args[i - 2]) == null) {
                        continue;
                    }
                    if (ps[i].getName().startsWith("get")) {
                        this.getDataMap.put(ps[i].getName().substring(4), args[i - 2]);
                    } else if (ps[i].getName().startsWith("post")) {
                        this.postDataMap.put(ps[i].getName().substring(5), args[i - 2]);
                    } else if (ps[i].getName().equals("arrParams")) {
                        this.arrParams = (String[]) args[i - 2];
                    } else {
                        data.put(ps[i].getName(), args[i - 2]);
                    }
                }
            }
        }
    }

    public void clearDateMap() {
        data.clear();
        getDataMap.clear();
        postDataMap.clear();
    }

}