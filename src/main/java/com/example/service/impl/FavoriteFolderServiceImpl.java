package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.entity.FavoriteFolder;
import com.example.service.FavoriteFolderService;
import com.example.mapper.FavoriteFolderMapper;
import org.springframework.stereotype.Service;

/**
* @author 31815
* @description 针对表【favorite_folder(收藏夹表)】的数据库操作Service实现
* @createDate 2025-02-24 12:04:16
*/
@Service
public class FavoriteFolderServiceImpl extends ServiceImpl<FavoriteFolderMapper, FavoriteFolder>
    implements FavoriteFolderService{

}




