package com.KillSystem.Service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.KillSystem.DAO.GoodsDao;
import com.KillSystem.Service.IFileService;
import com.KillSystem.util.FTPUtil;
import com.KillSystem.util.PropertiesUtil;
import com.google.common.collect.Lists;


/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 文件服务实现类
 * 封装了文件上传的服务
 * 
 * 2018年4月5日
 *
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private GoodsDao goodsDao;

    public String upload(MultipartFile file,String path){
    	return upload(file,path,0);
    }
    
    public String upload(MultipartFile file,String path,int goods_id){
    	String fileName = file.getOriginalFilename();
        //扩展名
        //abc.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = null;
        if(goods_id == 0) {
        	uploadFileName = UUID.randomUUID().toString()+"."+fileName.substring(fileName.lastIndexOf(".")+1);
        }else {
        	uploadFileName = goods_id + UUID.randomUUID().toString()+"."+fileName.substring(fileName.lastIndexOf(".")+1);
        }
        
        log.info("开始上传文件,上传文件的文件名:"+fileName+",上传的路径:"+path+",新文件名:"+uploadFileName);
        System.out.println(fileName+","+path+","+uploadFileName);
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);


        try {
            file.transferTo(targetFile);
            //文件已经上传成功了


            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传到ftp服务器上

            targetFile.delete();
        } catch (IOException e) {
            log.error("上传文件异常",e);
            return null;
        }
        
        goodsDao.updateGoodsImgurl(goods_id,PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFile.getName());

        
        //A:abc.jpg
        //B:abc.jpg
        return targetFile.getName();
    }

}