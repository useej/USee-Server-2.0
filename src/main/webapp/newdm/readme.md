# README

## 解决开发中AJAX跨域问题

由于在本地开发，存在跨域问题，临时解决方案：关闭web安全保护

```bash
google-chrome --arg --disable-web-security --user-data-dir=false
```

## 文件结构说明

引用了Bootstrap的CSS和JS，以及Bootstrap-DateTimePicker插件，所以文件较多，之后可以使用CDN

由于只是单个页面开发，没有考虑太多CSS的命名空间问题，所有主要文件命名也都是`index.*`
