package com.li.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.li.dao.Config;
import com.li.dao.MainWebClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Service {
    WebClient webClient;
    Properties p;
    HtmlPage home = null;

    public Service() {
        webClient = new MainWebClient().getWebClient();
        p = Config.getProperties();
    }

    public void work() {
        System.out.println("服务启动成功，将于设定时间自动打卡");
        while (true) {
            int now = Integer.parseInt(getTime());
            String time = Config.getProperties().getProperty("time");
            String[] times = time.split("-");
            int startTime = Integer.parseInt(times[0]);
            int endTime = Integer.parseInt(times[1]);
            if (now >= startTime && now < endTime) {
                for (int i = 0; i < 10; i++) {
                    submit();
                    if (confirm()) {
                        Mail.sendMail(p.getProperty("mail"), "自动打卡信息", getTimes()+" 打卡成功");
                        System.out.println(getTimes()+" 打卡成功");
                        try {
                            TimeUnit.HOURS.sleep(15);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    if (i == 9) {
                        Mail.sendMail(p.getProperty("mail"), "自动打卡信息", getTimes()+" 打卡失败，请手动打卡并检查此服务");
                        System.out.println(getTimes()+" 打卡失败，请手动打卡并检查此服务");
                    }
                }
            }
            try {
                TimeUnit.HOURS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void submit() {
        assert home != null;
        try {
            home = webClient.getPage(p.getProperty("url"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<HtmlTextInput> location = home.getByXPath("/html/body/div/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[1]/div/input");
        location.get(0).setText(p.getProperty("location"));

        String isInSchool = "是".equals(p.getProperty("isInschool")) ? "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[2]/div/label[1]/input" : "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[2]/div/label[2]/input";
        List<HtmlRadioButtonInput> isInSchoolList = home.getByXPath(isInSchool);
        isInSchoolList.get(0).setAttribute("checked", "checked");

        String studentIdentity = p.getProperty("studentIdentity");
        String identity = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[3]/div/label[1]/input";
        if ("1".equals(studentIdentity)) {
            identity = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[3]/div/label[1]/input";
        } else if ("2".equals(studentIdentity)) {
            identity = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[3]/div/label[2]/input";
        } else if ("3".equals(studentIdentity)) {
            identity = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[3]/div/label[3]/input";
        } else if ("4".equals(studentIdentity)) {
            identity = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[3]/div/label[4]/input";
        } else if ("5".equals(studentIdentity)) {
            identity = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[3]/div/label[5]/input";
        } else if ("6".equals(studentIdentity)) {
            identity = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[3]/div/label[6]/input";
        } else if ("7".equals(studentIdentity)) {
            identity = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[3]/div/label[7]/input";
        }
        List<HtmlRadioButtonInput> identityList = home.getByXPath(identity);
        identityList.get(0).setAttribute("checked", "checked");

        String pecialPersonnel = "是".equals(p.getProperty("pecialPersonnel")) ? "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[4]/div/label[1]/input" : "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[4]/div/label[2]/input";
        List<HtmlRadioButtonInput> pecialPersonnelList = home.getByXPath(pecialPersonnel);
        pecialPersonnelList.get(0).setAttribute("checked", "checked");

        String locationDenger = "是".equals(p.getProperty("locationDenger")) ? "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[5]/div/label[1]/input" : "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[5]/div/label[2]/input";
        List<HtmlRadioButtonInput> locationDengerList = home.getByXPath(locationDenger);
        locationDengerList.get(0).setAttribute("checked", "checked");

        String vaccines = p.getProperty("vaccines");
        String isVaccines = "是".equals(vaccines) ? "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[6]/div/label[1]/input" : "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[6]/div/label[2]/input";
        List<HtmlRadioButtonInput> vaccinesList = home.getByXPath(isVaccines);
        vaccinesList.get(0).setAttribute("checked", "checked");

        if ("是".equals(vaccines)) {
            String count = p.getProperty("vaccinesCount");
            String countLocation = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[7]/div/label[1]/input";
            if ("1".equals(count)) {
                countLocation = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[7]/div/label[1]/input";
            } else if ("2".equals(count)) {
                countLocation = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[7]/div/label[2]/input";
            } else if ("3".equals(count)) {
                countLocation = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[7]/div/label[3]/input";
            }
            List<HtmlRadioButtonInput> countList = home.getByXPath(countLocation);
            countList.get(0).setAttribute("checked", "checked");
        }

        List<HtmlTextInput> temperature = home.getByXPath("/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[8]/div/input");
        temperature.get(0).setText(p.getProperty("temperature"));

        String symptom = "是".equals(p.getProperty("symptom")) ? "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[9]/div/label[1]/input" : "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[9]/div/label[2]/input";
        List<HtmlRadioButtonInput> symptomList = home.getByXPath(symptom);
        symptomList.get(0).setAttribute("checked", "checked");

        String fever = "是".equals(p.getProperty("fever")) ? "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[10]/div/label[1]/input" : "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[10]/div/label[2]/input";
        List<HtmlRadioButtonInput> feverList = home.getByXPath(fever);
        feverList.get(0).setAttribute("checked", "checked");

        String diagnosis = "是".equals(p.getProperty("diagnosis")) ? "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[11]/div/label[1]/input" : "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[11]/div/label[2]/input";
        List<HtmlRadioButtonInput> diagnosisList = home.getByXPath(diagnosis);
        diagnosisList.get(0).setAttribute("checked", "checked");

        String suspected = "是".equals(p.getProperty("suspected")) ? "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[12]/div/label[1]/input" : "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[12]/div/label[2]/input";
        List<HtmlRadioButtonInput> suspectedList = home.getByXPath(suspected);
        suspectedList.get(0).setAttribute("checked", "checked");

        String been = "是".equals(p.getProperty("been")) ? "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[16]/div/label[1]/input" : "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[16]/div/label[2]/input";
        List<HtmlRadioButtonInput> beenList = home.getByXPath(been);
        beenList.get(0).setAttribute("checked", "checked");

        String closeContact = "是".equals(p.getProperty("closeContact")) ? "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[14]/div/label[1]/input" : "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[14]/div/label[2]/input";
        List<HtmlRadioButtonInput> closeContactList = home.getByXPath(closeContact);
        closeContactList.get(0).setAttribute("checked", "checked");

        String stay = "是".equals(p.getProperty("stay")) ? "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[15]/div/label[1]/input" : "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[15]/div/label[2]/input";
        List<HtmlRadioButtonInput> stayList = home.getByXPath(stay);
        stayList.get(0).setAttribute("checked", "checked");

        String parents = "是".equals(p.getProperty("parents")) ? "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[16]/div/label[1]/input" : "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[16]/div/label[2]/input";
        List<HtmlRadioButtonInput> parentsList = home.getByXPath(parents);
        parentsList.get(0).setAttribute("checked", "checked");

        String relatives = "是".equals(p.getProperty("relatives")) ? "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[17]/div/label[1]/input" : "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[17]/div/label[2]/input";
        List<HtmlRadioButtonInput> relativesList = home.getByXPath(relatives);
        relativesList.get(0).setAttribute("checked", "checked");

        String dayState = p.getProperty("dayState");
        String dayStateX = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[19]/div/label[2]/input";
        if ("1".equals(dayState)) {
            dayStateX = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[19]/div/label[1]/input";
        } else if ("2".equals(dayState)) {
            dayStateX = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[19]/div/label[2]/input";
        } else if ("3".equals(dayState)) {
            dayStateX = "/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[19]/div/label[3]/input";
        }
        List<HtmlRadioButtonInput> dayStateList = home.getByXPath(dayStateX);
        dayStateList.get(0).setAttribute("check", "checked");

        List<HtmlTextInput> telephone = home.getByXPath("/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[20]/div/input");
        telephone.get(0).setText(p.getProperty("telephone"));

        List<HtmlTextInput> parentName = home.getByXPath("/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[21]/div/input");
        parentName.get(0).setText(p.getProperty("parentName"));

        List<HtmlTextInput> parentPhone = home.getByXPath("/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[22]/div/input");
        parentPhone.get(0).setText(p.getProperty("parentPhone"));

        List<HtmlButton> button = home.getByXPath("/html/body/div[1]/div/div[3]/div/div[2]/div/div/div/div[1]/form/div[25]/button");
        try {
            button.get(0).click();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean confirm() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());
        try {
            home = webClient.getPage(p.getProperty("url") + "?op=getlist");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (home.asXml().contains(date)) {
            return true;
        }
        return false;
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("HH");
        return sdf.format(new Date());
    }

    public String getTimes() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}