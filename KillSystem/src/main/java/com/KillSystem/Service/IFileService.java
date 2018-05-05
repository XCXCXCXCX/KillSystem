package com.KillSystem.Service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 文件服务
 * 提供文件上传接口
 * 
 * 2018年4月5日
 *
 */
public interface IFileService {

    String upload(MultipartFile file, String path);

	String upload(MultipartFile file, String path, int goods_id);
}
