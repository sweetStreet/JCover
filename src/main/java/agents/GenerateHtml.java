package main.java.agents;

import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GenerateHtml {

    HashMap<String, String> sourceCodeMap = new HashMap<>();


    public void generateStmtHtml(String className, List<Integer> statements, Set<Integer> stmtcoverage){
        String souceCode;
        if(!sourceCodeMap.containsKey(className)){
            readSourceCode(className);
        }
        souceCode = sourceCodeMap.getOrDefault(className, "");
        if(!souceCode.equals("")){
            CreatStmtHtml(className.replace('/','_')+"_stmt.html", souceCode, statements, stmtcoverage);
        }
    }

    public void generateBranchHtml(String className, List<Integer> statements, Set<Integer> branchcoverage){
        String souceCode;
        if(!sourceCodeMap.containsKey(className)){
            readSourceCode(className);
        }
        souceCode = sourceCodeMap.getOrDefault(className, "");
        if(!souceCode.equals("")){
            CreatBranchHtml(className.replace('/','_')+"_branch.html", souceCode, statements, branchcoverage);
        }
    }

    public void readSourceCode(String className){
        try {
            BufferedReader in = new BufferedReader(new FileReader("src/main/java/"+className+".java"));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str+"\n");
            }
            sourceCodeMap.put(className, sb.toString());
        } catch (IOException e) {
            System.out.println("can not find");
        }
    }

    public static void CreatStmtHtml(String filePath, String sourceCode, List<Integer> statements, Set<Integer> stmtcoverage){
        String[] sourceCodeList = sourceCode.split("\n");
        int index = 0;

        //创建、初始化stringHtml对象
        StringBuilder stringHtml = new StringBuilder();
        //初始化文件对象
        PrintStream printStream =null;
        try{
            //打开文件
            printStream = new PrintStream(new FileOutputStream(filePath));
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        //追加输入HTML文件内容
        stringHtml.append("<html><head>");
        stringHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n");
        stringHtml.append("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js\"></script>\n");
        stringHtml.append("<title>Testing Report</title>");
        stringHtml.append("</head>");
        stringHtml.append("<body>");
        stringHtml.append("<pre>");
        while(index<sourceCodeList.length) {
//            String bgcolor = "background:#ffffff;";
//            if(statements.contains(index+1)){
//                bgcolor = "background:#fcf7b6;";
//            }
            stringHtml.append("<font style=\""+"font-weight:bold;");
            if(stmtcoverage.contains(index+1)) {
                stringHtml.append("color:#02d9c3;\"/>");
            }else if(statements.contains(index+1)){
                stringHtml.append("color:#ff69B4;\"/>");
            }else{
                stringHtml.append("color:black;\"/>");
            }
            stringHtml.append((index+1)+" "+sourceCodeList[index]+"\n");
            index++;
        }
        stringHtml.append("</pre>\n");
        String data = "["+stmtcoverage.size() +","+(statements.size() - stmtcoverage.size())+"]";
        DecimalFormat fmt = new DecimalFormat("##0.00");
        stringHtml.append("Statement Coverage Rate: "+fmt.format((double)stmtcoverage.size()/statements.size()) +"\n");
        stringHtml.append(
                "<div style=\"width:20%\">\n"+
                        "<canvas id=\"stmtCoverage\"></canvas>\n" +
                        "</div>\n"+
                        "<script>\n" +
                        "var ctx = document.getElementById('stmtCoverage');\n" +
                        "var stmtCoverage = new Chart(ctx, {\n" +
                        "    type: 'pie',\n" +
                        "    data: {\n" +
                        "        labels: ['executed', 'missed'],\n" +
                        "        datasets: [{\n" +
                        "           data:"+data+",\n"+
                        "           backgroundColor: [\n"+
                        "           'rgb(75, 192, 192)',\n"+
                        "           'rgb(255, 99, 132)',\n"+
                        "           ]\n"+
                        "        }]\n" +
                        "    },\n" +

                        "});\n" +
                        "</script>\n");
        stringHtml.append("</body></html>");
        try{
            //将HTML文件内容写入文件中
            printStream.println(stringHtml.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void CreatBranchHtml(String filePath, String sourceCode, List<Integer> statements, Set<Integer> branchcoverage){
        String[] sourceCodeList = sourceCode.split("\n");
        int index = 0;

        //创建、初始化stringHtml对象
        StringBuilder stringHtml = new StringBuilder();
        //初始化文件对象
        PrintStream printStream =null;
        try{
            //打开文件
            printStream = new PrintStream(new FileOutputStream(filePath));
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        //追加输入HTML文件内容
        stringHtml.append("<html><head>");
        stringHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n");
        stringHtml.append("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js\"></script>\n");
        stringHtml.append("<title>Testing Report</title>");
        stringHtml.append("</head>");
        stringHtml.append("<body>");
        stringHtml.append("<pre>");
        while(index<sourceCodeList.length) {
//            String bgcolor = "background:#ffffff;";
//            if(statements.contains(index+1)){
//                bgcolor = "background:#fcf7b6;";
//            }
            stringHtml.append("<font style=\""+"font-weight:bold;");
            if(branchcoverage.contains(index+1)) {
                stringHtml.append("color:#02d9c3;\"/>");
            }else if(statements.contains(index+1)){
                stringHtml.append("color:#ff69B4;\"/>");
            }else{
                stringHtml.append("color:black\"/>");
            }
            stringHtml.append((index+1)+" "+sourceCodeList[index]+"\n");
            index++;
        }
        stringHtml.append("</pre>\n");
        String data = "["+branchcoverage.size() +","+(statements.size() - branchcoverage.size())+"]";
        DecimalFormat fmt = new DecimalFormat("##0.00");
        stringHtml.append("Branch Coverage Rate: "+fmt.format((double)branchcoverage.size()/statements.size()) +"\n");
        stringHtml.append(
                "<div style=\"width:20%\">\n"+
                        "<canvas id=\"stmtCoverage\"></canvas>\n" +
                        "</div>\n"+
                        "<script>\n" +
                        "var ctx = document.getElementById('stmtCoverage');\n" +
                        "var stmtCoverage = new Chart(ctx, {\n" +
                        "    type: 'pie',\n" +
                        "    data: {\n" +
                        "        labels: ['executed', 'missed'],\n" +
                        "        datasets: [{\n" +
                        "           data:"+data+",\n"+
                        "           backgroundColor: [\n"+
                        "           'rgb(75, 192, 192)',\n"+
                        "           'rgb(255, 99, 132)',\n"+
                        "           ]\n"+
                        "        }]\n" +
                        "    },\n" +

                        "});\n" +
                        "</script>\n");
        stringHtml.append("</body></html>");
        try{
            //将HTML文件内容写入文件中
            printStream.println(stringHtml.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
