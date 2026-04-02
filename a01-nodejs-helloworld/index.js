// index.js

const dayjs = require('dayjs');

/**
 * NodeJS 的 HelloWorld
 * 对比 Java：不需要 class，不需要编译
 * 对比 Python：语法更接近 Java（花括号、分号）
 */
function hello() {
    console.log("🟢 Hello from NodeJS!");
    console.log(`当前时间: ${dayjs().format('YYYY-MM-DD HH:mm:ss')}`);

    // JS 的 Array ≈ Java 的 ArrayList ≈ Python 的 list
    const languages = ["Python", "Java", "NodeJS"];
    languages.forEach(lang => {
        console.log(`  正在学习: ${lang}`);
    });

    // JS 的 Object ≈ Java 的 HashMap ≈ Python 的 dict
    const scores = { Python: 90, Java: 95, NodeJS: 85 };
    Object.entries(scores).forEach(([lang, score]) => {
        console.log(`  ${lang} 分数: ${score}`);
    });
}

hello();