# Locust2.0

#### 介绍
新版本本体学习工具，在1.0版本基础上大幅改动和增强，纳入对能力问题（Competency Question，CQ）及多源异构输入的本体学习，提供后端接口，CQ可用于生成本体的验证。

#### 软件架构
软件架构说明
1.  框架
后端使用SpringBoot为主要开发框架，需适配低代码前端。开发中，初版形成之后完善。
2.  Restful-API
提供输入数据分析、验证、输出等控制器，暂以Restful-API的形式开放接口调用。
3.  开发依赖
部分开发依赖由Maven管理，具体依赖及其版本详见pom.xml文件。
4.  Apache Jena
此开发依赖为导入库，版本为4.7.0。
5.  文件路径
在Windows下执行，很多路径还用的绝对路径，在D盘中。
6. 数据库
使用MySQL数据库，schema名为myontology，建立库表的sql文件再resources目录下。
7. 序列化与反序列化
程序中涉及的全局变量使用反序列化进行初始化，使用序列化进行输出，对应逻辑在程序启动、退出时执行。
8. 能力问题库
能力问题库使用BigCQ，有若干能力问题模板和SPARQL-OWL查询模板。
9. 双向同步
处理完输入，若不是直接进入本地叙词表的，都要和本地叙词表之间进行一次双向同步，确保出现的词组在本地叙词表中都有。

#### 使用说明

1.  有关硬编码的文件路径等：第一版开发结束后改为读取配置文件的形式；
2.  提供Restful-API：二次开发前后端联调，初衷是支持领域专家和知识工程师协作；
3.  作为论文稿《需求驱动安全本体工程》（或类似名字的）开源代码；
4. 本开源仓库实现是为了小论文，距离可以分享复用还有差距，作者将长期维护该开源仓库；
5. 前端本身作为低代码平台实现，是一个创新点，但低代码平台开发量很大，且后端逻辑需十分完善，自身工作又比较偏工程，可以后慢慢实现。

#### 相关论文
1.  在写，延续Locust1.0内容，以及延伸修改内容；
2. Locust1.0: 在投OA，预备会议+期刊转投
3. Locust2.0: 预期B，期刊
4. 大论文相关：开题

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
