package com.pub.core.utils;

import com.github.pagehelper.PageHelper;
import com.pub.core.utils.sql.SqlUtil;
import com.pub.core.util.page.PageDomain;
import com.pub.core.util.page.TableSupport;

/**
 * 分页工具类
 * 
 * @author ruoyi
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据,一定要注意紧跟分页查询语句，不然回导致其它语句分页
     */
    public static void startPage()  {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String isAsc = pageDomain.getIsAsc();
        if (pageNum!=null && pageSize!=null)
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }else{
            //默认是第一页，10.条数据
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(1, 10, orderBy);
        }
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}
