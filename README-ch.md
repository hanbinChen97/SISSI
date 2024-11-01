# Should I Stay or Should I Go (SISSI)

SISSI 是一个基于用户停留时间和访问历史提供个性化酒吧推荐的安卓应用。系统通过分析用户当前位置和之前访问过的地点来计算推荐结果。

## 主要功能

- 实时GPS位置追踪
- Google地图导航集成
- 个性化酒吧推荐
- 已访问酒吧评分系统
- 详细的酒吧信息展示(包含描述和照片)
- 多语言支持

## 技术栈

### 安卓开发
- 使用Kotlin作为主要开发语言
- MVVM架构模式
- Android Jetpack组件
- 数据绑定
- LiveData与ViewModel
- 基于Fragment的UI设计

### 位置服务
- Google Maps API
- Google位置服务
- GPS追踪
- 地理围栏

### 网络通信
- Fuel HTTP客户端
- RESTful API集成
- JSON序列化/反序列化
- Kotlinx序列化

### 用户界面
- Material Design组件
- 自定义标签页导航
- 动态布局
- 使用Glide加载图片

### 测试
- JUnit单元测试
- Mockk进行Kotlin测试模拟
- Android仪器测试

### 构建工具
- Gradle
- Android SDK 29
- AndroidX库

## 演示

查看 [SISSI安卓演示视频](https://www.youtube.com/watch?v=Del5X4rqogw&ab_channel=egochen)

## 系统要求

- Android 6.0 (API 23) 或更高版本
- Google Play服务
- 网络连接
- 支持GPS的设备 