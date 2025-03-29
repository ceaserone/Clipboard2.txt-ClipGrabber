# ClipGrabber Keyboard

  💯✅🏕️👍👌👇😜👽🔥♥️

ClipGrabber is a custom Android keyboard that not only works as a minimalist text input method, but also passively logs every clipboard copy to a local text file — all while bypassing Android’s restrictive clipboard access policies (Android 10+). This is a completely unique utility that stays out of your way, but gets the job done.

---

✅**Features**:

▫️Minimalist custom keyboard with:

▫️Number row (0–9)

▫️QWERTY layout

▫️Spacebar, backspace, enter

▫️Foreground service to keep 
  clipboard monitoring alive

▫️Accessibility service support 
  (optional)

▫️Floating overlay with "Saving" 
   bubble so you know it's active

▫️Logs every new clipboard copy

▫️Saves clipboard entries to:
  /storage/emulated/0/Download/
  clipboard.txt

-------

❓Why do this❓

Android restricts any clipboard monitoring except by a custom keyboard, I couldn't find a single android app that simply let's you "super copy to clipboard" and have everything you copy instantly saved to a .txt file.

Android 10+ restricts clipboard access for background apps. ClipGrabber bypasses this by:

Registering as a custom keyboard (input method), which is allowed clipboard access

Running a foreground service and optional accessibility service

Providing a minimal, usable keyboard so it can be enabled system-wide

-------

❗Setup Instructions:

✅1. Install the APK

Build the project or install the release APK directly.

✅2. Grant All Permissions
(I added every permission I could, some might need to enable all to get by androids security and to be able to copy all "copy to clipboard" text, but at most install, enable file system, enable keyboard and start)

Manage External Storage

Display over Other Apps

Ignore Battery Optimization
(optional)

App will request these at first launch.
(if it doesn't do it yourself)

✅3. Enable Keyboard

Go to:

> Settings > System > Languages & Input > Keyboards > Manage Keyboards

✅Enable ClipGrabber

(Optional) Make it default keyboard for logging clipboard while you type

✅4. Enable Accessibility Service (optional but recommended)

This allows the app to keep monitoring clipboard even outside input fields.

> Settings > Accessibility > Installed Services > ClipGrabber

✅5. Start Monitoring

Open the app and tap “Turn ON”

A floating “Saving…” box will appear

Now, every time you copy text, it’s saved to the log file!

-------

✅Output Example:

clipboard.txt output:

My copied text here
---
Another copied line
---
https://example.com
---

-------

💯🔥##Why It’s Unique🔥💯

There is no other Android clipboard logger that works silently, reliably, and doesn’t require root or hacks. This works entirely within Android’s allowed system, just smartly designed:

Uses the keyboard to legally access clipboard

Minimal design

Doesn’t interrupt or crash other keyboards

-------

When You See “Saving…”

That little floating message means clipboard is being actively monitored. You can copy over and over from any app (Chrome, Notes, etc.) and every entry is safely saved to the .txt.

-------

Credits:
Built with love by Synack Network
Code + AI + Human collab by me and SAB, my AI copilot. 💯 ❤️👌🏕️😎

License:
MIT — use it, improve it, remix it.
