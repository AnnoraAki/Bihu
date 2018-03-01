# 逼乎

重庆邮电大学 红岩网校移动开发部 Android学员 2018年 寒假作业 lv3

![github](https://github.com/Cchanges/Bihu/blob/master/app/src/main/res/mipmap-xhdpi/logo.png)

## apk下载地址

[传送门](https://github.com/Cchanges/Bihu/blob/master/app/release/app-release.apk)

## 实现的功能

* 实现了作业要求给予的所有[逼乎功能](https://github.com/jay68/bihu_web/wiki/%E9%80%BC%E4%B9%8EAPI%E6%96%87%E6%A1%A3)

* 在此感谢子来学姐给的[获取七牛云token的工具](https://github.com/Zzzia/qiniuToken)

## 功能展示
* 用户注册\登陆

![github](https://github.com/Cchanges/Bihu/blob/master/gif/Animation1.gif)

* 发表问题（含图片）

![github](https://github.com/Cchanges/Bihu/blob/master/gif/Animation2.gif)

* 问题的点赞\点踩\收藏

![github](https://github.com/Cchanges/Bihu/blob/master/gif/Animation3.gif)

* 发表评论

![github](https://github.com/Cchanges/Bihu/blob/master/gif/Animation4.gif)

* 评论的点赞\点踩\采纳

![github](https://github.com/Cchanges/Bihu/blob/master/gif/Animation5.gif)

* 查看收藏列表并在收藏列表内操作

![github](https://github.com/Cchanges/Bihu/blob/master/gif/Animation6.gif)

* 更换头像

![github](https://github.com/Cchanges/Bihu/blob/master/gif/Animation7.gif)

>关于这个遇到一个问题，在模拟器端如果选择的图片是从电脑传过来的<br/>
或者是在模拟器上二次加工的图片再进行处理的时候会报错<br/>
根据log和debug之后发现是在`DocumentsContract.getDocumentId(uri)`<br/>
处理封装后的uri时候返回的并不是数字id而是路径...<br/>
>不知道这个是不是因为自己的代码判断问题还是什么...

## 通过这个“大”工程习得了
* 界面绘制/控件使用
  * `LinerLayout`
  * `ConstrainLayout`
  * 嵌套布局
  * 自定义`shape`
  * `svg`导入和加工
  * `anim`
  * `SwipeRefreshLayout`
  * `popupwindow`
  
* `HttpUrlConnection`

* 原生JSON解析

* `RecyclerView`
  * `Adapter`的设置
  * `ViewHolder`的设置
  * 通过暴露出来的方法对`Adapter`内部进行设置
  * 与`ScrollView`的滑动冲突

* `RadioGroup`/`RadioButton`的初步使用

* `Fragment`的初阶使用
  * `add`
  * `hide`
  * `show`
>了解到在使用的时候`add`和`replace`有冲突，根据查阅的资料修改为`hide`和`show`

* 自定义View的初步理解和简单绘制

* 自定义工具类  
  * 日期处理  
  * 图片处理  
  * 网络请求  
  * `Toast`自定义类

## 可能会添加的功能

* 图片裁剪<br/>
本想根据网上的原生教程实现的，但是在实测的时候才发现使用的时候相册程序会自动关闭<br/>
询问后才了解到不是每个api都支持隐式开启裁剪功能，有点坑<br/>
应该会找第三方库进行该功能的实现

* 用户一次登录后进入程序后自动登录<br/>
这个部分原理不难...但是...算了有空再说

* 数据保存<br/>
数据库保存网络获取的相关数据（问题列表/回答列表/头像）

## 可能有时间会完善的点

* 动画<br/>
`popupwindow`弹出时展现的动画，加载动画，进入app动画

* 回答的时候添加图片(虽然在实际方面感觉没什么意义)

* 代码的完善<br/>
了解需要的。再好好写工具类。<br/>
不然就会在写工具类的时候卡得要死甚至几个小时就这么没了...

## 开发者/完成者
[Cynthia](https://github.com/Cchanges)
