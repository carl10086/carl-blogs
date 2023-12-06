## 1. Intro

This project is used for carl-blogs.

## 2. Project Structure

- ./gradle/xxxx: `gradle` 的中控中心 ;
    - `meta-plugins`: 控制了 jvm 系的所有东西，例如 jvm 版本，kotlin 版本, spring boot 版本 等等
- `cb-protobuf`: 整体架构偏向 多语言, 这里是 `grpc` 的协议层
- `cb-relations`: 关系图谱核心代码实现, 语言 `KOTLIN`
- `cb-feeds`: feeds 流核心代码实现, 语言 `KOTLIN`, 选择推拉结合