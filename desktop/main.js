const { app, BrowserWindow, Menu, shell } = require("electron");
const path = require("path");

function createWindow() {
  const win = new BrowserWindow({
    width: 1100, height: 780, minWidth: 420, minHeight: 520,
    backgroundColor: "#151714",
  });
  Menu.setApplicationMenu(null);
  // Tautan eksternal dibuka di browser bawaan, bukan di dalam jendela aplikasi.
  win.webContents.setWindowOpenHandler(({ url }) => {
    shell.openExternal(url);
    return { action: "deny" };
  });
  win.loadFile(path.join(__dirname, "index.html"));
}

app.whenReady().then(createWindow);
app.on("window-all-closed", () => app.quit());
