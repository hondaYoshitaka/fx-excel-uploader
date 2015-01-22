fx-excel-uploader
=================
ExcelアップロードとViewerです。

<!-- Banner start -->
<img style="height:40px;" src="http://www.oracle.com/ocom/groups/public/@otn/documents/digitalasset/402460.gif" height="40" />&nbsp;
<img style="height:40px;" src="http://www-jp.mysql.com/common/logos/logo-mysql-170x115.png" height="40" />&nbsp;
<img style="height:40px;" src="http://maven.apache.org/images/logos/maven-feather.png" height="35" />&nbsp;
<img style="height:40px;" src="http://www.seasar.org/images/seasar_logo_blue.gif" height="40" />&nbsp;
<!-- Banner end -->

初期設定
-----
### mysqlの設定
```bash
# スキーマの作成
mysql> CREATE DATABASE fx_excel;
# ユーザの作成と権限の付与
mysql> GRANT ALL PRIVILEGES ON fx_excel.* TO 'fx_excel'@'localhost' IDENTIFIED BY 'fx_excel';
```
```bash
# テーブルの作成
> mysql -uroot -p fx_excel < {{プロジェクトDir}}/entity/src/test/resources/dump/fx_excel.dmp
```

アプリケーションの起動
-----
### tomcatサーバの起動
基底ディレクトリwebにおいて
```bash
> mvn tomcat7:run
```
http://tomcat.apache.org/maven-plugin-2.0/tomcat7-maven-plugin/

### JavaFX8 クライアントアプリの起動
```bash
> mvn jfx:run
```
https://github.com/zonski/javafx-maven-plugin
