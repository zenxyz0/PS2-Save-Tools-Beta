# PS2 Save Tools

![status](https://img.shields.io/badge/status-beta-orange) ![license](https://img.shields.io/badge/license-MIT-blue)

A single-page, browser-only toolbox for PlayStation 2 memory card saves. Nothing is uploaded: every file is processed locally in your browser.

> **Beta.** Tested with synthetic data only. Not yet verified on every real save, on a real PS2, or in PCSX2 for every card size. **Back up your memory cards before overwriting them.**

## Features

**Converter**
- Reads `.cbs` (CodeBreaker), `.psu` (EMS), `.xps` (SharkPort), and `.max` (PowerSave / MAX Drive) saves.
- Builds a ready-to-use `Mcd001.ps2` memory card image (with ECC) in **8, 16, 32, or 64 MB**.
- Shows a preview icon for each save and lets you tick which saves to include.
- Can add saves to an existing card (saves with the same folder name are replaced) and can enlarge a card. Existing saves show icons and checkboxes; untick one to drop it from the new card.
- Reads the finished card back and verifies the save, file, and size counts.

**Reader**
- Lists saves with title (from `icon.sys`), folder name, date, file count, size, and a small icon preview.
- Search, sort, capacity bar, export a save to `.psu`.
- Tick saves to delete them, with undo until you download the result.

**Copy Between Cards**
- Load two cards (or create an empty one), then copy or move selected saves in either direction.

## Usage

Open the hosted page at <https://zenxyz0.github.io/PS2-Save-Tools-Beta/> (GitHub Pages), or download `index.html` from this repository and open it in any modern browser. The standalone file saves a plain `.ps2`; the sandboxed preview offers a `.zip` instead.

## Status and known limitations

| Area | Status |
| --- | --- |
| Card layout checked against the mymc reference and its documentation | Done |
| Round-trip and unit tests with synthetic data (all sizes, delete, copy, merge) | Done |
| Real `.cbs` / `.psu` / `.xps` files | Not yet tested |
| Real `.max` files (LZARI decoder) | Only tested against my own encoder |
| 3D save icons | Best-effort, unverified; falls back to a plain icon |
| PCSX2 / real PS2 with generated cards, especially 16/32/64 MB | Not yet verified |

A real PS2 only recognizes 8 MB cards. The 16/32/64 MB sizes are meant for PCSX2 and devices that support them. The 64 MB option needs roughly 130 MB of temporary browser memory.

Renaming a save folder is intentionally not supported: games look their saves up by that name.

## Reporting problems and ideas

Use the issue forms: [Report a bug](https://github.com/zenxyz0/PS2-Save-Tools-Beta/issues/new?template=bug_report.yml) or [Request a feature](https://github.com/zenxyz0/PS2-Save-Tools-Beta/issues/new?template=feature_request.yml). The app also shows a "Report this on GitHub" link next to Converter errors that pre-fills the form with the error text. Please include the card size, the save format, the browser, and for PCSX2 problems the PCSX2 version.

## Development

The whole app is one HTML file with inline CSS and JavaScript. The only external dependency is [pako](https://github.com/nodeca/pako) 2.1.0, loaded from cdnjs; for offline use, download `pako.min.js` into the repo and change the `<script src>` tag.

## Credits

- **Creator:** Zen
- **Assistant:** Claude.ai
- Format references: [mymc](https://github.com/ps2dev/mymc) by Ross Ridge (public domain) and the save format documentation of the Apollo project.
- [pako](https://github.com/nodeca/pako) (MIT) for zlib.
- LZARI algorithm by Haruhiko Okumura.

## License

MIT, see [LICENSE](LICENSE).
