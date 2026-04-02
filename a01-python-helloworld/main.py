# main.py

import requests

def hello():
    """
    Python 的 HelloWorld
    对比 Java：不需要 class，不需要 public static void main
    """
    print("🐍 Hello from Python!")
    print(f"requests 库版本: {requests.__version__}")

    # Python 的 list ≈ Java 的 ArrayList
    languages = ["Python", "Java", "NodeJS"]
    for lang in languages:
        print(f"  正在学习: {lang}")

    # Python 的 dict ≈ Java 的 HashMap
    scores = {"Python": 90, "Java": 95, "NodeJS": 85}
    for lang, score in scores.items():
        print(f"  {lang} 分数: {score}")

if __name__ == "__main__":
    hello()