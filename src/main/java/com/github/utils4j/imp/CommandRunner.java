/*
* MIT License
* 
* Copyright (c) 2022 Leonardo de Lima Oliveira
* 
* https://github.com/l3onardo-oliv3ira
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/


package com.github.utils4j.imp;

import static com.github.utils4j.imp.Directory.stringPath;

import java.io.File;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.ShellAPI;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinUser;

public class CommandRunner {
  
  public static void runAsAdmin(File script, String... args) throws Exception {
    String scriptPath = stringPath(script, true);
    String arguments = Strings.toString(args, ' ');
    throwIf(script == null || !script.exists(), scriptPath, arguments);
    ShellAPI.SHELLEXECUTEINFO info = new ShellAPI.SHELLEXECUTEINFO();
    info.lpFile = scriptPath;    
    info.lpParameters = arguments;
    info.nShow = WinUser.SW_SHOWDEFAULT;
    info.fMask = Shell32.SEE_MASK_NOCLOSEPROCESS;
    info.lpVerb = "runas";
    throwIf(!Shell32.INSTANCE.ShellExecuteEx(info), scriptPath, arguments);
    Kernel32.INSTANCE.WaitForSingleObject(info.hProcess, WinBase.INFINITE);
  }

  private static void throwIf(boolean condition, String scriptPath, String arguments) throws Exception {
    if (condition) {
      throw new Exception("Unabled to run script '" + scriptPath + "' with args [" + arguments + "]");
    }
  }
}
