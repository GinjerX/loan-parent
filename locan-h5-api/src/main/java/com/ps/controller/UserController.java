package com.ps.controller;

import com.ps.domain.MessageVO;
import com.ps.domain.UserVO;
import com.ps.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService service;
    @Value("${upload_path}")
    String uploadPath;

    @Value("jdbc.username")
    String username;
    @Value("jdbc.url")
    String url;
    @PostConstruct
    public void ann(){
        System.out.println(username);
        System.out.println(url);
    }

    /**
     * 查询用户个人信息
     *
     * @return
     */
    @RequestMapping("/userQuery")
    public MessageVO query(@RequestParam String phone) {
        MessageVO messageVO = new MessageVO();
        UserVO user = service.query(phone);
        messageVO.setData(user);
        messageVO.setCode(200);//状态码
        messageVO.setMsg("查询成功");
        return messageVO;
    }

    /**
     * 查询用户信息
     *
     * @return
     */
    @RequestMapping("/userQueryAll")
    public MessageVO queryAll() {
        MessageVO messageVO = new MessageVO();
        List<UserVO> list = service.queryUserAll();
        System.out.println("list:" + list);
        if (list != null) {
            messageVO.setData(list);
            messageVO.setCode(200);//状态码
            messageVO.setMsg("查询成功");
        }
        return messageVO;
    }

    /**
     * 编辑用户信息（ 修改）
     *
     * @return
     */
    @RequestMapping("/userUpdate")
    public MessageVO update(@RequestBody UserVO userVO) {
        MessageVO messageVO = new MessageVO();
        service.update(userVO);
        messageVO.setMsg("修改成功");
        return messageVO;
    }

    /**
     * 每一个用户拥有一个存储照片的文件夹 上传即创建 （需要判断是否已经存在）
     * 每一张照片拥有不同的编号
     * part1:身份证正面
     * part2:身份证反面
     * part3:银行卡正面
     */
    @PostMapping("/upload")
    public void upload(@RequestParam String id, @RequestParam Part part1, @RequestParam Part part2, @RequestParam Part part3) throws IOException {
        //1.获取存储地址
        String getUserPath = getPath(id);

        Path[] paths = {Paths.get(getUserPath, "idFront.png"), Paths.get(getUserPath, "idReverse.png"), Paths.get(getUserPath, "bankCard.png")};
        InputStream[] inputStream = {part1.getInputStream(), part2.getInputStream(), part3.getInputStream()};

        for (int i = 0; i < paths.length; i++) {
            Files.copy(inputStream[i], paths[i], StandardCopyOption.REPLACE_EXISTING);
        }

        UserVO userVO = new UserVO();
        userVO.setId(Integer.valueOf(id));
        userVO.setBankImg(getUserPath + "/bankCard.png");
        userVO.setIdentityCardardF(getUserPath + "/idFront.png");
        userVO.setIdentityCardardR(getUserPath + "/idReverse.png");

        service.upload(userVO);

    }

    @RequestMapping("/upload2")
    public void upload2(@RequestParam String phone, @RequestParam MultipartFile[] uploadImages) throws IOException {

        System.out.println(uploadImages.length + ":" + phone);
        //1.获取存储地址
        String getUserPath = getPath(phone);

        for (MultipartFile uploadImage : uploadImages) {

            InputStream is = uploadImage.getInputStream();
            System.out.println("文件名称：" + uploadImage.getName() + ":" + uploadImage.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(getUserPath + "\\" + uploadImage.getName());

            byte[] bytes = new byte[is.available()];
            is.read(bytes);

            fos.write(bytes);

            is.close();
            fos.close();

            System.out.println(uploadImage.getName());
        }
    }

    public String getPath(String id) {//创建用户存储地址

        String path = uploadPath + id;
        File file = new File(path);
        if (!file.exists()) {//如果文件夹不存在
            file.mkdir();//创建文件夹
        }
        return path;
    }

    public String mapupload(@RequestParam String id, @RequestParam Map<String, MultipartFile> fileMap) throws IOException {

        for (String s : fileMap.keySet()) {
            String path = getPath(id);
            MultipartFile map = fileMap.get(s);
            FileUtils.copyInputStreamToFile(map.getInputStream(),new File(path+"/"+map.getOriginalFilename()));
        }

        return "编辑通过";
    }
}
