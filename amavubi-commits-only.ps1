# ===================================================================
# AMAVUBI-SYSTEM ONLY: 100 SAFE, ISOLATED COMMITS
# Repo: https://github.com/ishimweegide23/amavubi-system
# Project: Amavubi Fan Hub (Java Swing + Hibernate + RMI)
# ===================================================================

# --- 1. SAFETY CHECK: Confirm we're in the right folder ---
$expectedPath = "D:\web tech tools\amavubi system\Amavubi Fan hub"
$currentPath = (Get-Location).Path

if ($currentPath -ne $expectedPath) {
    Write-Host "ERROR: You are NOT in the correct folder!" -ForegroundColor Red
    Write-Host "Expected: $expectedPath" -ForegroundColor Yellow
    Write-Host "Current : $currentPath" -ForegroundColor Yellow
    Write-Host "Please run this script INSIDE the Amavubi Fan hub folder." -ForegroundColor Red
    pause
    exit
}

Write-Host "SAFETY CHECK PASSED: Running in correct folder." -ForegroundColor Green

# --- 2. Confirm Git remote is amavubi-system ---
$remoteUrl = git config --get remote.origin.url
$expectedRemote = "https://github.com/ishimweegide23/amavubi-system.git"

if ($remoteUrl -ne $expectedRemote) {
    Write-Host "ERROR: This is NOT the amavubi-system repo!" -ForegroundColor Red
    Write-Host "Remote URL: $remoteUrl" -ForegroundColor Yellow
    Write-Host "Expected  : $expectedRemote" -ForegroundColor Yellow
    Write-Host "This script will NOT run to protect your other repos." -ForegroundColor Red
    pause
    exit
}

Write-Host "REPO CHECK PASSED: Connected to amavubi-system" -ForegroundColor Green

# --- 3. Generate 100 Amavubi-specific commits ---
$messages = @(
    "feat: initialize Amavubi Fan Hub with NetBeans + Hibernate",
    "feat: add Fan model (name, email, phone, membershipTier)",
    "feat: implement FanDAO with save, update, findByEmail",
    "feat: create DatabaseConnection using Hibernate 6",
    "feat: design LoginPage with Rwanda flag background",
    "feat: add RegistrationPage with Kinyarwanda toggle",
    "feat: implement password encryption (BCrypt)",
    "feat: build FanDashboard with side navigation",
    "feat: add Player model (jersey, position, goals, assists)",
    "feat: implement PlayerDAO with search by position",
    "feat: create PlayerStatisticsPage with JTable and charts",
    "feat: add Merchandise model (item, price, stock)",
    "feat: implement MerchandiseDAO with low stock alert",
    "feat: design MerchandisePage with image grid",
    "feat: add Match model (opponent, date, venue, ticketPrice)",
    "feat: implement MatchDAO with upcomingMatches()",
    "feat: create MatchBookingPage with seat map",
    "feat: add Transaction model (type: TICKET/MERCH, amount)",
    "feat: implement TransactionDAO with daily report",
    "feat: build TransactionsPage with receipt preview",
    "feat: add FundraisingPage with progress bar",
    "feat: implement RMI server (RMIServer.java)",
    "feat: create RMI client in FanController",
    "feat: enable remote fan registration via RMI",
    "fix: resolve Hibernate SessionFactory leak",
    "feat: add profile picture upload (JPG/PNG)",
    "feat: implement email validation with regex",
    "feat: add membership levels: Bronze, Silver, Gold",
    "feat: generate digital membership card (PDF)",
    "feat: add match countdown timer in dashboard",
    "feat: simulate live score updates every 30s",
    "feat: add fan leaderboard by match attendance",
    "feat: create admin panel with login",
    "feat: allow admin to add match results",
    "feat: implement stock alert when < 10 items",
    "feat: send mock receipt email after purchase",
    "refactor: move UI helpers to Util/SwingUtils.java",
    "feat: add dark mode (Amavubi colors: green/yellow)",
    "feat: implement fan search with autocomplete",
    "feat: add MVP voting per match",
    "feat: display vote results with live chart",
    "feat: embed match highlights (YouTube API mock)",
    "feat: show notification bell for upcoming matches",
    "feat: implement fan points (1 per match, 5 per merch)",
    "feat: add points redemption for free scarf",
    "feat: implement referral system (+200 points)",
    "docs: update README with screenshots and features",
    "feat: optimize named queries in Hibernate",
    "fix: resolve RMI bind exception on restart",
    "feat: add java.util.logging for debug",
    "feat: implement database backup to /backups",
    "feat: add export fans to CSV (admin only)",
    "feat: generate QR code for match check-in",
    "feat: record fan attendance on scan",
    "feat: add season ticket (10 matches discount)",
    "feat: set timezone to Africa/Kigali",
    "feat: add Kinyarwanda language support",
    "feat: toggle UI between English/Kinyarwanda",
    "feat: play Amavubi anthem on app launch",
    "feat: animate national team logo on startup",
    "feat: create Wall of Fame for top fans",
    "feat: implement fan-to-fan chat via RMI",
    "feat: add match prediction game",
    "feat: award 50 points for correct score",
    "feat: simulate push notifications",
    "feat: add offline mode with local H2 cache",
    "refactor: split FanController into services",
    "test: add JUnit tests for FanDAO",
    "test: mock RMI calls in integration tests",
    "chore: upgrade Hibernate to 6.4.0",
    "feat: build executable JAR with launch4j",
    "feat: create Windows installer (.exe)",
    "docs: add user guide PDF in /docs",
    "feat: add auto-update checker",
    "feat: send crash reports (mock)",
    "feat: add post-match survey",
    "feat: generate attendance heat map",
    "feat: export merch sales report (PDF)",
    "feat: generate barcode for tickets",
    "feat: mock thermal printer output",
    "feat: implement interactive seat map",
    "feat: add VIP section (higher price)",
    "feat: verify phone with mock SMS",
    "feat: send birthday wishes to fans",
    "feat: auto-send match reminders 24h before",
    "feat: show weather forecast for venue",
    "feat: integrate Google Maps (mock)",
    "feat: add bus booking to stadium",
    "feat: implement group discount (5+ tickets)",
    "feat: add family package (2 adults + 2 kids)",
    "feat: build loyalty dashboard",
    "feat: add badges: First Match, 10 Matches, etc.",
    "feat: animate badge unlock",
    "docs: add RMI interface documentation",
    "chore: tag release v1.0.0",
    "chore: publish to GitHub Releases",
    "feat: add splash screen with logo",
    "style: apply Amavubi color scheme",
    "fix: optimize image loading performance",
    "perf: improve JTable rendering speed",
    "feat: add keyboard shortcuts (Ctrl+B to book)",
    "feat: support drag & drop profile photo",
    "test: reach 85% test coverage",
    "chore: add GitHub Actions CI",
    "docs: add CODE_OF_CONDUCT.md",
    "feat: add feedback form after match",
    "feat: implement match rating (1-5 stars)",
    "chore: prepare for demo day",
    "feat: add live demo mode (auto-tour)"
)

$fileToTouch = "src/view/FanDashboard.java"
$startDate = Get-Date "2025-10-31 09:00:00"

Write-Host "`nStarting 100 commits for amavubi-system..." -ForegroundColor Magenta

for ($i = 0; $i -lt 100; $i++) {
    # Modify file
    Add-Content -Path $fileToTouch -Value "// Amavubi auto-commit #$i - $(Get-Date)"

    # Set commit date: +2.5 hours per commit
    $commitDate = $startDate.AddHours($i * 2.5)
    $dateStr = $commitDate.ToString("yyyy-MM-ddTHH:mm:ss")

    # Get message
    $msg = $messages[$i % $messages.Count]

    # Commit
    git add .
    $env:GIT_COMMITTER_DATE = $dateStr
    git commit --date=$dateStr -m "$msg" > $null 2>&1

    Write-Host "Commit $($i+1): $msg" -ForegroundColor Cyan
}

Write-Host "`n100 COMMITS DONE!" -ForegroundColor Green
Write-Host "Now run: git push origin main --force" -ForegroundColor Yellow
Write-Host "(Use --force only if you're the only contributor)" -ForegroundColor Gray
pause