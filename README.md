# sms forwarding
## 这个应用可以将短信转发至指定的邮箱

## 为什么写这个app？

因为我目前在国外，有时候需要用国内的手机收验证码，操作起来就不是很方便，就写了这么一个app来把国内的手机上的短信发到邮箱里方便查看。

## 如何使用？

在```/android_app/MyApplication/app/src/main/assets```文件夹下创建名为```email.properties```的文件，并写入如下内容：

```Bash
mail.smtp.host=smtp服务器地址
mail.smtp.port=smtp服务端口号
mail.smtp.auth=true
mail.smtp.starttls.enable=true
mail.user=用户名@XXX.YYY
mail.password=密码
mail.sendto=接收短信的邮箱
```

在android studio中打开```MyApplication```（注意不是android_app），连接手机后运行。在软件界面上打开开关，并使应用保存在后台（即使用HOME键使软件停留在后台，建议同时设置不被清理）。
