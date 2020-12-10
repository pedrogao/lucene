# lucene-1.4.3 源码阅读

## 目录

```bash
├── analysis        构建索引；分词器，过滤器
│   ├── de
│   ├── ru
│   └── standard
├── document        文档、字段
├── index           索引；组织和访问索引
├── queryParser     查询解析，借助JavaCC实现，类似于antlr工具
├── search          搜索索引
│   └── spans
├── store           存储；存储索引
└── util            工具类
```

## 其它

- 因为 lucene 构建基于 ant，但是我们已经不用 ant 了，所以带上了 `.idea` 目录方便直接在 idea 中使用。
