import sys
import json
import time
import os
import traceback

def main():
    try:
        # 1. 取得這支 Python 腳本自己所在的「絕對資料夾路徑」
        current_script_dir = os.path.dirname(os.path.abspath(__file__))
        
        # 2. 動態組合出 data 資料夾的絕對路徑
        target_dir = os.path.join(current_script_dir, "data")
        
        # 自動把 crash_log.txt 放在跟腳本同一個地方，方便排錯
        CRASH_LOG_PATH = os.path.join(current_script_dir, "crash_log.txt")

        # --------------------------------------------------
        input_data = sys.stdin.read()
        if not input_data.strip():
            sys.stdout.write(input_data)
            return

        payload = json.loads(input_data)
        timestamp = int(time.time() * 1000)
        filename = f"before_tool_{timestamp}.json"
        
        # 自動建立資料夾
        if not os.path.exists(target_dir):
            os.makedirs(target_dir)
            
        output_path = os.path.join(target_dir, filename)
        
        # 3. 🎯 寫入美化對齊後的 JSON 格式
        with open(output_path, 'w', encoding='utf-8') as f:
            # indent=4: 自動縮排 4 個空格，並優雅地換行
            # ensure_ascii=False: 保持中文、專案內 spec 標籤的原始文字，不轉成編碼
            json.dump(payload, f, indent=4, ensure_ascii=False)
            
        if os.path.exists(CRASH_LOG_PATH):
            os.remove(CRASH_LOG_PATH)

        print(f"\n[Hook 成功] 美化版數據已寫入: {output_path}", file=sys.stderr)

    except Exception as e:
        # 萬一失敗，動態寫入腳本旁的 crash_log
        with open(CRASH_LOG_PATH, 'w', encoding='utf-8') as error_file:
            error_file.write(f"錯誤訊息: {str(e)}\n")
            error_file.write(traceback.format_exc())
        print(f"\n[Hook 失敗] 錯誤已記錄至: {CRASH_LOG_PATH}", file=sys.stderr)

    # 4. 🔥 永遠要原封不動還給 Gemini CLI
    sys.stdout.write(input_data)

if __name__ == "__main__":
    main()