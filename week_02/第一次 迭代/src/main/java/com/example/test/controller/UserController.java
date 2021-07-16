package com.example.test.controller;

import com.example.test.bean.CommitBean;
import com.example.test.bean.UserBean;
import com.example.test.service.UserService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    //查询所有的user数据
    @RequestMapping("/findByAll")
    public String index(Model model) {
        List<UserBean> list = userService.findByAll();
        model.addAttribute("users",list);
        return "userList";
    }

    @RequestMapping("/manager")
    public String list(Model model) {
        List<UserBean> users = userService.findByAll();
        model.addAttribute("users", users);
        List<CommitBean> commits = userService.findAllCommit();
        model.addAttribute("commits",commits);
        return "userList";
    }

    //保存user数据
    @RequestMapping("/toAdd")
    public String toAdd() {
        return "userAdd";
    }

    @RequestMapping("/add")
    public String add(UserBean user) {
        userService.saveUser(user);
        return "redirect:/manager";
    }

    //修改user数据
    @RequestMapping("/toEdit")
    public String toEdit(Model model, Integer id) {
        UserBean user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "userEdit";
    }

    @RequestMapping("toEditPass")
    public String toEditPass(Model model,Integer id,String userId,String name,String job,String exp,String fileSrc) throws IOException {
        userService.updateCommit(id);
        //生成文件,存到本地
        Document document = new Document(PageSize.A4, 60, 60, 60, 60);
        try {
            //PdfWriter.getInstance(document, new FileOutputStream("F:\\Hello simplePDF.pdf"));
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, byteStream);
            document.addAuthor(userId);
            document.addCreationDate();
            document.addCreator("song");
            document.addSubject("pass");
            document.addTitle("you have already pass");
            document.addHeader("Expires", "0");
            document.open();
            //向文件中添加文字
            Chunk glue = new Chunk(new VerticalPositionMark());//用来隔断到一行的两边
            Paragraph paragraph1 = new Paragraph("恭喜您通过了审核", new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED),24, Font.BOLD));
            String str = "审核id:"+id.toString()+","+"用户id："+userId+","+"用户名字："+name+","+"用户群体："+job+","+"经历："+exp;
            Chunk timeChunk = new Chunk(str, new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED),18, Font.NORMAL));
            paragraph1.add(glue);
            paragraph1.add(timeChunk);
            paragraph1.setSpacingAfter(10);//距离后面行距
            document.add(paragraph1);
            //添加照片
            String filename = fileSrc.substring(4);
            filename = "D:\\temp-rainy\\"+filename ;
            Image image = Image.getInstance(filename);
            //改变宽高来适应
            image.setAbsolutePosition(220, 386);
            image.scaleToFit(600, 600);
            image.scalePercent(15);
            document.add(image);
            System.out.println(filename);
            //关闭文件
            document.close();
            byte[] buffer = byteStream.toByteArray();
            FileOutputStream fos = new FileOutputStream("D:\\temp-rainy\\Instruction.pdf");
            fos.write(buffer);
            fos.close();
        } catch (Exception ex) {
        }
        //读取本地文件转化成multipartfile类型
        File pdfFile = new File( "D:\\temp-rainy\\Instruction.pdf");
        FileInputStream fileInputStream = new FileInputStream(pdfFile);
        MultipartFile file = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(),
                ContentType.APPLICATION_OCTET_STREAM.toString(),
                fileInputStream);

        //生成新的文件传入本地映像的文件夹中
        String fileName = file.getName();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String filePath = "D://temp-rainy//";
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = "img/" + fileName;

        // 把路径插入数据库
        userService.updateCommitFile(id,filename);
        return "redirect:/manager";
    }

    @RequestMapping("toEditUnPass")
    public String toEditUnPass(Integer id,String userId) throws Exception{
        //更改审核状态
        userService.updateCommitUnPass(id);
        //使文件路径为空
        userService.updateCommitFile(id,null);
        Integer user = Integer.valueOf(userId);
        UserBean userBean = userService.findUserById(user);
        //获取对应id的这个用户的邮箱
        String email = userBean.getEmail();
        if (email != null) {
            //没有通过的话，发送用户邮件
            Properties properties = new Properties();
            properties.setProperty("mail.transport.protocol", "smtp");
            properties.setProperty("mail.smtp.auth", "true");
            Session session = Session.getInstance(properties);
            session.setDebug(true);//debug模式
            //邮件信息
            Message messgae = new MimeMessage(session);
            messgae.setFrom(new InternetAddress("2574507872@qq.com"));
            messgae.setText("你的审核没有通过耶，你再交一下？");
            messgae.setSubject(userBean.getName()+"的审核结果");//设置邮件主题
            //发送邮件
            Transport tran = session.getTransport();
            tran.connect("smtp.qq.com", 587, "2574507872@qq.com", "stwadydrnjstecgc");//连接到QQ邮箱服务器
            tran.sendMessage(messgae, new Address[]{new InternetAddress(email)});//设置邮件接收人
            tran.close();
        }
        return "redirect:/manager";
    }

    @RequestMapping("/edit")
    //@ResponseBody
    public String edit(UserBean user) {
        userService.updateUser(user);
        return "redirect:/manager";
    }

    //删除user数据
    @RequestMapping("/delete")
    public String delete(Integer id) {
        userService.deleteUser(id);
        return "redirect:/manager";
    }


}
