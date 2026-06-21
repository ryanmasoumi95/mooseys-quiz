package com.example.web_game;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    @GetMapping("/")
    public String home() {
        StringBuilder sb = new StringBuilder();
        sb.append("""
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Moosey's Mighty Quiz</title>
            <style>
                @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;700;800&family=DM+Sans:wght@400;500&display=swap');
                :root {
                    --bg: #0a0a0f; --surface: #13131e; --card: #1c1c2e;
                    --border: rgba(255,255,255,0.08); --text: #e8e8f0;
                    --muted: #6b6b8a; --correct: #22c55e; --wrong: #ef4444; --streak: #f59e0b;
                }
                * { box-sizing: border-box; margin: 0; padding: 0; }
                body { background: var(--bg); color: var(--text); font-family: 'DM Sans', sans-serif;
                    min-height: 100vh; display: flex; flex-direction: column; align-items: center; padding: 24px 16px; }
                header { width: 100%; max-width: 960px; display: flex; justify-content: space-between;
                    align-items: center; margin-bottom: 32px; }
                .logo { font-family: 'Syne', sans-serif; font-weight: 800; font-size: 26px; letter-spacing: -0.5px; }
                .logo span { background: linear-gradient(90deg, #a78bfa, #38bdf8);
                    -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
                .scoreboard { display: flex; gap: 20px; font-size: 14px; color: var(--muted); }
                .scoreboard b { color: var(--text); font-size: 17px; }
                .screen { width: 100%; max-width: 960px; display: none; }
                .screen.active { display: block; }
                #category-screen h2 { font-family: 'Syne', sans-serif; font-size: 36px; font-weight: 800;
                    text-align: center; margin-bottom: 8px; color: #fff; }
                #category-screen .tagline { text-align: center; color: var(--muted); margin-bottom: 32px; font-size: 15px; }
                .category-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(160px, 1fr)); gap: 14px; }
                .cat-card { background: var(--card); border: 1px solid var(--border); border-radius: 16px;
                    padding: 22px 16px; text-align: center; cursor: pointer;
                    transition: transform 0.15s, border-color 0.15s; position: relative; overflow: hidden; }
                .cat-card::before { content: ''; position: absolute; inset: 0; opacity: 0;
                    transition: opacity 0.2s; border-radius: 16px; }
                .cat-card[data-hue]::before { background: radial-gradient(circle at 50% 0%, hsla(var(--h),70%,60%,0.18), transparent 70%); }
                .cat-card:hover { transform: translateY(-4px); }
                .cat-card:hover::before { opacity: 1; }
                .cat-card .emoji { font-size: 34px; display: block; margin-bottom: 10px; }
                .cat-card .name { font-family: 'Syne', sans-serif; font-weight: 700; font-size: 13px;
                    letter-spacing: 0.5px; text-transform: uppercase; }
                .cat-card .best { font-size: 11px; color: var(--muted); margin-top: 5px; }
                .difficulty-row { display: flex; gap: 8px; justify-content: center; margin-bottom: 28px; flex-wrap: wrap; }
                .diff-pill { padding: 7px 18px; border-radius: 99px; border: 1px solid var(--border);
                    background: var(--card); color: var(--muted); font-size: 13px; font-weight: 500;
                    cursor: pointer; transition: all 0.15s; }
                .diff-pill.active, .diff-pill:hover { border-color: #a78bfa; color: #fff; background: rgba(167,139,250,0.12); }
                #quiz-screen { animation: fadeUp 0.3s ease; }
                @keyframes fadeUp { from { opacity:0; transform:translateY(16px); } to { opacity:1; transform:translateY(0); } }
                .quiz-header { display: flex; align-items: center; gap: 14px; margin-bottom: 24px; }
                .cat-badge { background: var(--card); border: 1px solid var(--border); border-radius: 10px;
                    padding: 6px 12px; font-family: 'Syne', sans-serif; font-weight: 700; font-size: 14px; }
                .streak-badge { margin-left: auto; background: rgba(245,158,11,0.15);
                    border: 1px solid rgba(245,158,11,0.3); color: var(--streak); border-radius: 10px;
                    padding: 6px 14px; font-weight: 700; font-size: 14px; display: none; }
                .streak-badge.visible { display: block; }
                .progress-bar { height: 4px; background: var(--border); border-radius: 99px; margin-bottom: 28px; overflow: hidden; }
                .progress-fill { height: 100%; background: linear-gradient(90deg, #a78bfa, #38bdf8);
                    border-radius: 99px; transition: width 0.4s ease; }
                .q-number { font-size: 12px; color: var(--muted); text-transform: uppercase; letter-spacing: 1px; margin-bottom: 12px; }
                .q-text { font-family: 'Syne', sans-serif; font-size: 22px; font-weight: 700;
                    line-height: 1.4; margin-bottom: 28px; min-height: 60px; color: #fff; }
                .options-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-bottom: 20px; }
                @media (max-width: 560px) { .options-grid { grid-template-columns: 1fr; } }
                .opt-btn { background: var(--card); border: 1px solid var(--border); border-radius: 14px;
                    padding: 16px 20px; color: var(--text); font-family: 'DM Sans', sans-serif; font-size: 15px;
                    cursor: pointer; text-align: left; transition: background 0.12s, border-color 0.12s, transform 0.1s;
                    display: flex; align-items: center; gap: 12px; }
                .opt-btn:hover:not(:disabled) { background: #252538; border-color: #a78bfa; transform: translateX(3px); }
                .opt-btn .letter { width: 28px; height: 28px; border-radius: 8px; background: rgba(255,255,255,0.06);
                    display: flex; align-items: center; justify-content: center; font-weight: 700;
                    font-size: 12px; flex-shrink: 0; font-family: 'Syne', sans-serif; }
                .opt-btn.correct { background: rgba(34,197,94,0.12); border-color: var(--correct); color: var(--correct); }
                .opt-btn.correct .letter { background: var(--correct); color: #000; }
                .opt-btn.wrong { background: rgba(239,68,68,0.1); border-color: var(--wrong); color: var(--wrong); }
                .opt-btn.wrong .letter { background: var(--wrong); color: #fff; }
                .opt-btn:disabled { cursor: default; }
                .feedback-bar { border-radius: 12px; padding: 14px 18px; font-size: 15px; font-weight: 500;
                    display: none; align-items: center; gap: 10px; margin-bottom: 16px;
                    animation: popIn 0.2s cubic-bezier(0.34,1.56,0.64,1); }
                @keyframes popIn { from { transform: scale(0.92); opacity: 0; } to { transform: scale(1); opacity: 1; } }
                .feedback-bar.show { display: flex; }
                .feedback-bar.correct { background: rgba(34,197,94,0.12); border: 1px solid rgba(34,197,94,0.3); color: var(--correct); }
                .feedback-bar.wrong { background: rgba(239,68,68,0.1); border: 1px solid rgba(239,68,68,0.3); color: var(--wrong); }
                .next-btn { width: 100%; padding: 15px; border: none; border-radius: 14px;
                    background: linear-gradient(135deg, #a78bfa, #38bdf8); color: #000;
                    font-family: 'Syne', sans-serif; font-weight: 700; font-size: 16px; cursor: pointer;
                    display: none; transition: opacity 0.15s, transform 0.15s; }
                .next-btn:hover { opacity: 0.9; transform: scale(1.01); }
                .next-btn.show { display: block; }
                #results-screen { text-align: center; animation: fadeUp 0.3s ease; }
                .results-emoji { font-size: 72px; margin-bottom: 16px; }
                .results-title { font-family: 'Syne', sans-serif; font-weight: 800; font-size: 40px; color: #fff; margin-bottom: 8px; }
                .results-score-big { font-family: 'Syne', sans-serif; font-size: 80px; font-weight: 800;
                    background: linear-gradient(90deg, #a78bfa, #38bdf8); -webkit-background-clip: text;
                    -webkit-text-fill-color: transparent; line-height: 1; margin: 16px 0; }
                .results-stats { display: flex; justify-content: center; gap: 32px; margin: 24px 0 36px;
                    font-size: 14px; color: var(--muted); }
                .results-stats .stat b { display: block; font-size: 22px; color: var(--text); margin-bottom: 2px; }
                .results-actions { display: flex; gap: 12px; justify-content: center; flex-wrap: wrap; }
                .btn-primary { padding: 14px 32px; border: none; border-radius: 14px;
                    background: linear-gradient(135deg, #a78bfa, #38bdf8); color: #000;
                    font-family: 'Syne', sans-serif; font-weight: 700; font-size: 16px; cursor: pointer; transition: opacity 0.15s; }
                .btn-primary:hover { opacity: 0.88; }
                .btn-secondary { padding: 14px 32px; border: 1px solid var(--border); border-radius: 14px;
                    background: var(--card); color: var(--text); font-family: 'Syne', sans-serif;
                    font-weight: 700; font-size: 16px; cursor: pointer; transition: border-color 0.15s; }
                .btn-secondary:hover { border-color: #a78bfa; }
                .best-badge { display: inline-block; background: rgba(245,158,11,0.15);
                    border: 1px solid rgba(245,158,11,0.4); color: var(--streak); border-radius: 10px;
                    padding: 5px 14px; font-size: 13px; font-weight: 700; margin-top: 8px; }
                .review-list { margin-top: 32px; text-align: left; display: flex; flex-direction: column; gap: 10px; }
                .review-item { background: var(--card); border: 1px solid var(--border); border-radius: 12px;
                    padding: 14px 16px; font-size: 14px; display: flex; gap: 10px; align-items: flex-start; }
                .review-icon { font-size: 16px; flex-shrink: 0; margin-top: 1px; }
                .review-q { color: var(--muted); font-size: 12px; margin-bottom: 3px; }
                .review-a { font-weight: 500; }
            </style>
        </head>
        <body>
        <header>
            <div class="logo" onclick="goHome()" style="cursor:pointer">🦌 Moosey's <span>Mighty Quiz</span></div>
            <div class="scoreboard">
                <div>Total Points <b id="total-pts">0</b></div>
                <div>Best Streak <b id="best-streak-hdr">0</b></div>
            </div>
        </header>

        <div id="category-screen" class="screen active">
            <h2>Choose Your Category</h2>

            <div class="difficulty-row">
                <div class="diff-pill active" data-diff="all">🎲 All</div>
                <div class="diff-pill" data-diff="easy">🌱 Easy</div>
                <div class="diff-pill" data-diff="medium">🔥 Medium</div>
                <div class="diff-pill" data-diff="hard">💀 Hard</div>
            </div>
            <div class="category-grid" id="cat-grid"></div>
        </div>

        <div id="quiz-screen" class="screen">
            <div class="quiz-header">
                <button onclick="goHome()" style="background:none;border:2px solid #a78bfa;border-radius:10px;padding:6px 14px;color:#a78bfa;cursor:pointer;font-size:14px;font-weight:600;transition:background 0.15s,color 0.15s;">← Back</button>
                <div class="cat-badge" id="quiz-cat-badge"></div>
                <div class="streak-badge" id="streak-badge">🔥 <span id="streak-count">0</span> streak</div>
            </div>
            <div class="progress-bar"><div class="progress-fill" id="progress-fill" style="width:0%"></div></div>
            <div id="quiz-body"></div>
        </div>

        <div id="results-screen" class="screen">
            <div class="results-emoji" id="res-emoji">🧠</div>
            <div class="results-title" id="res-title">Nice work!</div>
            <div class="results-score-big" id="res-score">+0</div>
            <div id="best-badge-wrap"></div>
            <div class="results-stats">
                <div class="stat"><b id="res-correct">0</b>Correct</div>
                <div class="stat"><b id="res-wrong">0</b>Wrong</div>
                <div class="stat"><b id="res-streak">0</b>Best Streak</div>
            </div>
            <div class="results-actions">
                <button class="btn-primary" onclick="playAgainSame()">Play Again ↺</button>
                <button class="btn-secondary" onclick="goHome()">All Categories</button>
            </div>
            <div class="review-list" id="review-list"></div>
        </div>

        <script>
        // ── QUESTION BANK ──────────────────────────────────────────────────────────
        const BANK = {
            gk: [
                {q:"What is the chemical symbol for gold?",o:["Au","Ag","Fe","Pb"],c:0,d:"easy",f:"Au comes from the Latin word Aurum."},
                {q:"How many sides does a hexagon have?",o:["5","6","7","8"],c:1,d:"easy",f:"The word hexagon comes from Greek hex meaning six."},
                {q:"What is the capital of Australia?",o:["Sydney","Melbourne","Brisbane","Canberra"],c:3,d:"easy",f:"Canberra was chosen as a compromise between Sydney and Melbourne in 1908."},
                {q:"How many bones are in the adult human body?",o:["206","185","230","212"],c:0,d:"easy",f:"Babies are born with around 270 bones; many fuse together as they grow."},
                {q:"What is the largest ocean on Earth?",o:["Atlantic","Indian","Arctic","Pacific"],c:3,d:"easy",f:"The Pacific Ocean covers more area than all of Earth's land combined."},
                {q:"Which country invented the telephone?",o:["USA","UK","Canada","Scotland"],c:3,d:"medium",f:"Alexander Graham Bell was born in Edinburgh, Scotland, making him Scottish."},
                {q:"What is the hardest natural substance on Earth?",o:["Ruby","Steel","Diamond","Quartz"],c:2,d:"easy",f:"Diamond scores 10 on the Mohs hardness scale, the maximum possible."},
                {q:"How many hearts does an octopus have?",o:["1","2","3","4"],c:2,d:"medium",f:"Two hearts pump blood to the gills; the third pumps it to the rest of the body."},
                {q:"What percentage of the Earth is covered by water?",o:["51%","61%","71%","81%"],c:2,d:"medium",f:"Despite this, only about 3% of Earth's water is fresh water."},
                {q:"Which planet rotates on its side?",o:["Saturn","Neptune","Uranus","Jupiter"],c:2,d:"medium",f:"Uranus has an axial tilt of 97.77 degrees, likely caused by a giant collision."},
""");
        sb.append("""
                {q:"What is the most spoken language in the world by native speakers?",o:["English","Spanish","Hindi","Mandarin Chinese"],c:3,d:"medium",f:"Mandarin has over 900 million native speakers, far ahead of any other language."},
                {q:"What is the square root of 144?",o:["11","12","13","14"],c:1,d:"easy",f:"144 is also called a gross — a dozen dozens."},
                {q:"Which element has the symbol Fe?",o:["Fluorine","Iron","Francium","Fermium"],c:1,d:"easy",f:"Fe comes from the Latin word Ferrum, meaning iron."},
                {q:"How long does light from the Sun take to reach Earth?",o:["About 8 minutes","About 1 minute","About 1 hour","About 30 seconds"],c:0,d:"medium",f:"The exact average is 8 minutes and 20 seconds, depending on Earth's orbit."},
                {q:"What is the only mammal capable of true flight?",o:["Flying squirrel","Bat","Sugar glider","Colugo"],c:1,d:"easy",f:"All other so-called flying mammals only glide; bats are the sole true fliers."}
            ],
            history: [
                {q:"In what year did World War II end?",o:["1943","1944","1945","1946"],c:2,d:"easy",f:"Japan formally surrendered on 2 September 1945, ending the war."},
                {q:"Who was the first President of the United States?",o:["John Adams","Thomas Jefferson","Benjamin Franklin","George Washington"],c:3,d:"easy",f:"Washington was unanimously elected and served two terms from 1789 to 1797."},
                {q:"Which ancient wonder was located in Alexandria?",o:["The Colossus","The Lighthouse","The Mausoleum","The Statue of Zeus"],c:1,d:"medium",f:"The Lighthouse of Alexandria stood over 100 metres tall and guided ships for centuries."},
                {q:"What year did the Berlin Wall fall?",o:["1987","1988","1989","1991"],c:2,d:"easy",f:"The wall fell on 9 November 1989, symbolising the end of the Cold War."},
                {q:"Who was the first woman to win a Nobel Prize?",o:["Rosalind Franklin","Marie Curie","Dorothy Hodgkin","Ada Lovelace"],c:1,d:"easy",f:"Marie Curie won twice — in Physics (1903) and Chemistry (1911)."},
                {q:"Which empire was ruled by Genghis Khan?",o:["Ottoman Empire","Roman Empire","Mongol Empire","Persian Empire"],c:2,d:"easy",f:"At its peak, the Mongol Empire was the largest contiguous land empire in history."},
                {q:"In which year did the Titanic sink?",o:["1910","1911","1912","1913"],c:2,d:"easy",f:"The Titanic sank on 15 April 1912 after striking an iceberg the night before."},
                {q:"Which country did Adolf Hitler originally come from?",o:["Germany","Austria","Switzerland","Poland"],c:1,d:"medium",f:"Hitler was born in Braunau am Inn, Austria, in 1889."},
                {q:"What was the name of the first artificial satellite launched into space?",o:["Explorer 1","Luna 1","Vostok","Sputnik 1"],c:3,d:"medium",f:"Sputnik 1 was launched by the Soviet Union on 4 October 1957."},
                {q:"The French Revolution began in which year?",o:["1776","1783","1789","1799"],c:2,d:"medium",f:"The storming of the Bastille on 14 July 1789 is considered its symbolic start."},
                {q:"Who was the Egyptian queen famously associated with Julius Caesar and Mark Antony?",o:["Nefertiti","Hatshepsut","Cleopatra","Isis"],c:2,d:"easy",f:"Cleopatra VII spoke nine languages and was the last active ruler of Ptolemaic Egypt."},
                {q:"Which country fired the first shots of World War I?",o:["Germany","Austria-Hungary","Serbia","Russia"],c:1,d:"hard",f:"Austria-Hungary declared war on Serbia on 28 July 1914."},
                {q:"The ancient city of Carthage was located in which modern country?",o:["Libya","Algeria","Morocco","Tunisia"],c:3,d:"hard",f:"Carthage, near modern-day Tunis, was destroyed by Rome in 146 BC."},
                {q:"Who discovered penicillin?",o:["Louis Pasteur","Joseph Lister","Alexander Fleming","Robert Koch"],c:2,d:"medium",f:"Fleming noticed mould killing bacteria on a petri dish in 1928 — by accident."},
                {q:"The Battle of Hastings was fought in which year?",o:["1042","1066","1077","1088"],c:1,d:"medium",f:"William the Conqueror defeated King Harold II on 14 October 1066."}
            ],
            science: [
                {q:"What is the powerhouse of the cell?",o:["Nucleus","Ribosome","Mitochondria","Golgi body"],c:2,d:"easy",f:"Mitochondria produce ATP, the energy currency used by cells."},
                {q:"What is the chemical formula for water?",o:["HO","H2O","H3O","HO2"],c:1,d:"easy",f:"Two hydrogen atoms bonded to one oxygen atom — simple but essential to all life."},
                {q:"What force keeps planets in orbit around the Sun?",o:["Magnetism","Nuclear force","Friction","Gravity"],c:3,d:"easy",f:"Gravity is an attractive force between any two objects with mass."},
                {q:"What is the speed of light in a vacuum?",o:["200,000 km/s","300,000 km/s","400,000 km/s","150,000 km/s"],c:1,d:"medium",f:"Precisely 299,792,458 metres per second — it's so exact it defines the metre."},
                {q:"How many chromosomes do humans typically have?",o:["23","44","46","48"],c:2,d:"medium",f:"Humans have 46 chromosomes arranged in 23 pairs — one from each parent."},
                {q:"What is the most abundant gas in Earth's atmosphere?",o:["Oxygen","Carbon dioxide","Argon","Nitrogen"],c:3,d:"easy",f:"Nitrogen makes up about 78% of the atmosphere; oxygen is around 21%."},
                {q:"What type of animal is a Komodo dragon?",o:["Crocodilian","Snake","Lizard","Tortoise"],c:2,d:"easy",f:"It is the world's largest living lizard, found on Indonesian islands."},
                {q:"What is the pH of pure water?",o:["5","6","7","8"],c:2,d:"easy",f:"A pH of 7 is neutral — below is acidic, above is alkaline."},
                {q:"Which planet has the most moons?",o:["Jupiter","Saturn","Uranus","Neptune"],c:1,d:"medium",f:"Saturn overtook Jupiter in 2023 with the discovery of 62 new moons, bringing its total over 140."},
                {q:"What is the name of the process by which plants make food from sunlight?",o:["Respiration","Transpiration","Photosynthesis","Osmosis"],c:2,d:"easy",f:"Photosynthesis converts carbon dioxide and water into glucose and oxygen using sunlight."},
                {q:"What is the atomic number of carbon?",o:["4","6","8","12"],c:1,d:"medium",f:"Carbon-12 is the most abundant isotope and defines the atomic mass unit."},
                {q:"What is the name of the force that opposes motion?",o:["Tension","Inertia","Momentum","Friction"],c:3,d:"easy",f:"Friction is caused by the microscopic roughness of surfaces in contact."},
                {q:"Which scientist proposed the theory of general relativity?",o:["Isaac Newton","Niels Bohr","Albert Einstein","Max Planck"],c:2,d:"easy",f:"Einstein published his general theory of relativity in 1915."},
                {q:"DNA stands for what?",o:["Dioxyribonucleic Acid","Deoxyribonucleic Acid","Dinitrogen Acid","Deoxyribonitric Acid"],c:1,d:"medium",f:"DNA's double helix structure was discovered by Watson and Crick in 1953."},
                {q:"What is the boiling point of water at sea level in Celsius?",o:["90°C","95°C","100°C","105°C"],c:2,d:"easy",f:"At higher altitudes, water boils at lower temperatures due to reduced air pressure."}
            ],
            geography: [
                {q:"What is the longest river in the world?",o:["Amazon","Congo","Yangtze","Nile"],c:3,d:"easy",f:"The Nile stretches approximately 6,650 km through northeastern Africa."},
                {q:"Which country has the largest land area?",o:["China","USA","Canada","Russia"],c:3,d:"easy",f:"Russia covers over 17 million square kilometres — about twice the size of Antarctica."},
                {q:"What is the smallest country in the world?",o:["Monaco","San Marino","Liechtenstein","Vatican City"],c:3,d:"easy",f:"Vatican City covers just 0.44 square kilometres within Rome, Italy."},
                {q:"Which mountain is the tallest in the world?",o:["K2","Kangchenjunga","Lhotse","Mount Everest"],c:3,d:"easy",f:"Everest stands at 8,848.86 metres above sea level, remeasured in 2020."},
                {q:"Which African country has the largest population?",o:["Ethiopia","Egypt","South Africa","Nigeria"],c:3,d:"medium",f:"Nigeria has over 220 million people and is projected to become the 3rd most populous country by 2050."},
                {q:"What is the capital of Canada?",o:["Toronto","Vancouver","Ottawa","Montreal"],c:2,d:"easy",f:"Ottawa has been Canada's capital since 1857, chosen by Queen Victoria."},
                {q:"The Amazon rainforest is primarily located in which country?",o:["Colombia","Peru","Bolivia","Brazil"],c:3,d:"easy",f:"About 60% of the Amazon rainforest lies within Brazil's borders."},
                {q:"Which ocean is the smallest?",o:["Southern","Indian","Arctic","Atlantic"],c:2,d:"medium",f:"The Arctic Ocean covers about 14 million square kilometres, largely frozen year-round."},
                {q:"What is the largest desert in the world?",o:["Arabian","Gobi","Sahara","Antarctic"],c:3,d:"hard",f:"Antarctica is a cold desert receiving less than 200mm of precipitation per year."},
                {q:"The Strait of Gibraltar separates Europe from which continent?",o:["Asia","South America","Africa","North America"],c:2,d:"medium",f:"At its narrowest, the strait is only 14 kilometres wide."},
                {q:"Which country has the most natural lakes?",o:["Russia","Finland","USA","Canada"],c:3,d:"hard",f:"Canada contains roughly 60% of the world's lakes, over 2 million of them."},
                {q:"What is the capital of New Zealand?",o:["Auckland","Wellington","Christchurch","Dunedin"],c:1,d:"medium",f:"Wellington is the world's southernmost national capital city."},
                {q:"The Danube river flows into which sea?",o:["Caspian Sea","Mediterranean Sea","Black Sea","Adriatic Sea"],c:2,d:"hard",f:"The Danube passes through 10 countries — more than any other river in the world."},
                {q:"Which US state is the largest by area?",o:["Texas","Montana","California","Alaska"],c:3,d:"easy",f:"Alaska is more than twice the size of Texas, the second-largest state."},
                {q:"In which country is the Serengeti National Park located?",o:["Kenya","South Africa","Botswana","Tanzania"],c:3,d:"medium",f:"The Serengeti hosts the largest terrestrial animal migration on Earth."}
            ],
            sports: [
                {q:"How many players are on a standard football (soccer) team?",o:["9","10","11","12"],c:2,d:"easy",f:"11 players per side, including the goalkeeper, making 22 on the pitch total."},
                {q:"Which country has won the most FIFA World Cup titles?",o:["Germany","Argentina","Italy","Brazil"],c:3,d:"easy",f:"Brazil has won 5 World Cups: 1958, 1962, 1970, 1994, and 2002."},
                {q:"In tennis, what score comes after Deuce?",o:["Match point","Advantage","Set point","Love"],c:1,d:"easy",f:"After deuce, a player must win two consecutive points to win the game."},
                {q:"How many rings are on the Olympic flag?",o:["4","5","6","7"],c:1,d:"easy",f:"The five rings represent the five continents united by the Olympic movement."},
                {q:"What is the maximum score in a single bowling game?",o:["250","275","300","320"],c:2,d:"medium",f:"A perfect game of 300 requires 12 consecutive strikes."},
                {q:"Which sport uses a shuttlecock?",o:["Squash","Badminton","Racquetball","Lacrosse"],c:1,d:"easy",f:"A shuttlecock can travel at over 300 km/h in professional badminton."},
                {q:"How long is a marathon in kilometres?",o:["40km","41.09km","42.195km","43km"],c:2,d:"medium",f:"The distance was standardised at 42.195 km at the 1908 London Olympics."},
                {q:"In which sport would you perform a 'slam dunk'?",o:["Volleyball","Water polo","Basketball","Handball"],c:2,d:"easy",f:"The slam dunk became legal in college basketball again in 1976 after a brief ban."},
                {q:"What is the national sport of Japan?",o:["Judo","Kendo","Sumo","Karate"],c:2,d:"medium",f:"Sumo wrestling has been practised in Japan for over 1,500 years."},
                {q:"In cricket, how many balls are in a standard over?",o:["4","5","6","8"],c:2,d:"easy",f:"Six legal deliveries make an over; the bowler changes after each over."},
                {q:"Which country invented basketball?",o:["USA","Canada","UK","France"],c:1,d:"medium",f:"James Naismith, a Canadian, invented basketball in Springfield, Massachusetts in 1891."},
                {q:"At which Olympics did Usain Bolt first win the 100m gold medal?",o:["Athens 2004","Beijing 2008","London 2012","Rio 2016"],c:1,d:"medium",f:"Bolt ran 9.69 seconds in Beijing and then broke his own world record in the final."},
                {q:"How many Grand Slam tennis tournaments are held each year?",o:["2","3","4","5"],c:2,d:"easy",f:"Australian Open, French Open, Wimbledon, and US Open make up the four Grand Slams."},
                {q:"What colour jersey does the leader wear in the Tour de France?",o:["Red","White","Green","Yellow"],c:3,d:"easy",f:"The maillot jaune (yellow jersey) has been worn since 1919 to represent the race leader."},
                {q:"In American football, how many points is a touchdown worth?",o:["3","5","6","7"],c:2,d:"easy",f:"A touchdown scores 6 points; teams can then attempt a 1-point extra kick or a 2-point conversion."}
            ],
            movies: [
                {q:"Who directed 'Jurassic Park'?",o:["James Cameron","George Lucas","Steven Spielberg","Ridley Scott"],c:2,d:"easy",f:"Spielberg directed it in 1993, based on Michael Crichton's 1990 novel."},
                {q:"Which film features the line 'You had me at hello'?",o:["Sleepless in Seattle","Pretty Woman","Jerry Maguire","Notting Hill"],c:2,d:"medium",f:"The line is spoken by Renee Zellweger to Tom Cruise in the 1996 film."},
                {q:"What is the highest-grossing film of all time (adjusted for inflation)?",o:["Avengers: Endgame","Titanic","Star Wars: A New Hope","Gone with the Wind"],c:3,d:"hard",f:"Gone with the Wind (1939) remains the all-time box office champion when adjusted for inflation."},
                {q:"Which actor played Iron Man in the Marvel Cinematic Universe?",o:["Chris Evans","Chris Hemsworth","Robert Downey Jr.","Mark Ruffalo"],c:2,d:"easy",f:"Robert Downey Jr. played Tony Stark / Iron Man in 10 MCU films."},
                {q:"In The Matrix, does Neo take the red or blue pill?",o:["Blue","Red","Both","Neither"],c:1,d:"easy",f:"The red pill reveals the truth of the Matrix; the blue pill would have kept him in blissful ignorance."},
                {q:"Which film won the first ever Academy Award for Best Picture?",o:["Wings","Sunrise","The Jazz Singer","All Quiet on the Western Front"],c:0,d:"hard",f:"Wings (1927) won the inaugural Best Picture award at the 1st Academy Awards in 1929."},
                {q:"Who voiced Woody in Toy Story?",o:["John Goodman","Tim Allen","Tom Hanks","Billy Crystal"],c:2,d:"easy",f:"Tom Hanks has voiced Woody across all four Toy Story films."},
                {q:"Which country produces the most films annually?",o:["USA","China","UK","India"],c:3,d:"medium",f:"India's film industry, commonly known as Bollywood, produces over 1,500 films per year."},
                {q:"What year was the original Star Wars released?",o:["1975","1976","1977","1978"],c:2,d:"easy",f:"Star Wars: A New Hope was released on 25 May 1977 and changed cinema forever."},
                {q:"Which film features a character named Forrest Gump?",o:["Cast Away","Philadelphia","Big","Forrest Gump"],c:3,d:"easy",f:"Tom Hanks won an Oscar for Best Actor for the role in 1994."},
                {q:"Who directed 'Pulp Fiction'?",o:["Martin Scorsese","David Fincher","Quentin Tarantino","Coen Brothers"],c:2,d:"easy",f:"Pulp Fiction won the Palme d'Or at Cannes in 1994 and reinvented indie cinema."},
                {q:"Which film contains the famous shower scene with Janet Leigh?",o:["Vertigo","The Birds","Psycho","Rear Window"],c:2,d:"medium",f:"Hitchcock's Psycho (1960) — the scene took 7 days to film and uses 70 camera angles."},
                {q:"What animated film features a rat who wants to be a chef in Paris?",o:["A Bug's Life","Ratatouille","Finding Nemo","The Incredibles"],c:1,d:"easy",f:"Ratatouille (2007) was praised for its loving tribute to French cuisine and cooking culture."},
                {q:"In which film does the phrase 'I'll be back' originate?",o:["Predator","Total Recall","RoboCop","The Terminator"],c:3,d:"easy",f:"Arnold Schwarzenegger said it in the 1984 original; it became one of cinema's most iconic lines."},
                {q:"Which director made 'Schindler's List'?",o:["Stanley Kubrick","Francis Ford Coppola","Steven Spielberg","Oliver Stone"],c:2,d:"easy",f:"Spielberg refused a salary for directing it, calling it blood money, and donated it to Holocaust education."}
            ],
            music: [
                {q:"Which band released 'Bohemian Rhapsody'?",o:["Led Zeppelin","The Rolling Stones","Queen","The Beatles"],c:2,d:"easy",f:"Released in 1975, it was one of the first music videos and broke multiple songwriting rules."},
                {q:"How many strings does a standard guitar have?",o:["4","5","6","7"],c:2,d:"easy",f:"Classical and acoustic guitars have 6 strings; bass guitars typically have 4."},
                {q:"Who is known as the 'King of Pop'?",o:["Elvis Presley","Prince","Michael Jackson","David Bowie"],c:2,d:"easy",f:"Michael Jackson sold an estimated 350 million records worldwide."},
                {q:"Which country does the musical genre 'reggae' originate from?",o:["Cuba","Trinidad","Barbados","Jamaica"],c:3,d:"easy",f:"Reggae emerged in Jamaica in the late 1960s and was popularised globally by Bob Marley."},
                {q:"How many symphonies did Beethoven compose?",o:["7","8","9","10"],c:2,d:"medium",f:"His 9th Symphony, including Ode to Joy, was composed after he became completely deaf."},
                {q:"Which instrument has 88 keys?",o:["Organ","Harpsichord","Piano","Synthesizer"],c:2,d:"easy",f:"A standard piano has 52 white keys and 36 black keys spanning over 7 octaves."},
                {q:"Who sang 'Purple Rain'?",o:["David Bowie","Elton John","Prince","Stevie Wonder"],c:2,d:"easy",f:"Prince wrote, produced, and performed the song for his 1984 semi-autobiographical film."},
                {q:"What is the tempo marking 'Allegro' in music?",o:["Very slow","Moderate","Fast","Very fast"],c:2,d:"medium",f:"Allegro literally means 'lively' in Italian and typically indicates 120-156 beats per minute."},
                {q:"Which British band had a hit with 'Wonderwall' in 1995?",o:["Blur","Pulp","Oasis","Suede"],c:2,d:"easy",f:"It was written by Noel Gallagher and reportedly named after a 1968 George Harrison album."},
                {q:"Mozart was born in which city?",o:["Vienna","Munich","Prague","Salzburg"],c:3,d:"medium",f:"Wolfgang Amadeus Mozart was born in Salzburg, Austria, on 27 January 1756."},
                {q:"Which artist released the album 'Thriller'?",o:["Prince","James Brown","Michael Jackson","Whitney Houston"],c:2,d:"easy",f:"Thriller (1982) is the best-selling album of all time with over 66 million copies sold."},
                {q:"What does 'forte' mean in music?",o:["Slow","Soft","Loud","Fast"],c:2,d:"medium",f:"Forte (f) means loud; fortissimo (ff) means very loud; piano (p) means soft."},
                {q:"Which famous music venue is located in Nashville, Tennessee?",o:["Radio City Music Hall","Red Rocks","Carnegie Hall","Grand Ole Opry"],c:3,d:"medium",f:"The Grand Ole Opry has been broadcasting live country music radio since 1925."},
                {q:"Who composed 'The Four Seasons'?",o:["Bach","Handel","Vivaldi","Haydn"],c:2,d:"medium",f:"Antonio Vivaldi composed the four violin concertos around 1720, each depicting a different season."},
                {q:"Which female artist has won the most Grammy Awards?",o:["Adele","Whitney Houston","Mariah Carey","Beyonce"],c:3,d:"hard",f:"Beyonce has won 32 Grammy Awards, more than any other artist in history."}
            ],
            literature: [
                {q:"Who wrote 'Pride and Prejudice'?",o:["Charlotte Bronte","George Eliot","Virginia Woolf","Jane Austen"],c:3,d:"easy",f:"Austen originally titled the novel First Impressions before publishing it in 1813."},
                {q:"Which Shakespeare play features the character Shylock?",o:["Othello","The Merchant of Venice","Much Ado About Nothing","Hamlet"],c:1,d:"medium",f:"Shylock is the antagonist who demands a pound of flesh as collateral for a loan."},
                {q:"Who wrote '1984'?",o:["Aldous Huxley","Ray Bradbury","George Orwell","H.G. Wells"],c:2,d:"easy",f:"George Orwell wrote it in 1948 and named it by reversing the last two digits of the year."},
                {q:"In which country did the fairy tales of the Brothers Grimm originate?",o:["Austria","Switzerland","Denmark","Germany"],c:3,d:"easy",f:"Jacob and Wilhelm Grimm collected and published folk tales from German-speaking regions."},
                {q:"Who wrote 'The Great Gatsby'?",o:["Ernest Hemingway","John Steinbeck","F. Scott Fitzgerald","William Faulkner"],c:2,d:"easy",f:"Published in 1925, it is widely considered the great American novel."},
                {q:"What is the name of the whale in 'Moby Dick'?",o:["White Whale","Leviathan","Moby Dick","The Great Whale"],c:2,d:"easy",f:"Herman Melville was inspired partly by the true story of a whale called Mocha Dick."},
                {q:"Who wrote 'Harry Potter'?",o:["J.R.R. Tolkien","C.S. Lewis","Philip Pullman","J.K. Rowling"],c:3,d:"easy",f:"J.K. Rowling wrote the first book while a single mother living on welfare in Edinburgh."},
                {q:"In which novel would you find the character Atticus Finch?",o:["Of Mice and Men","To Kill a Mockingbird","The Grapes of Wrath","East of Eden"],c:1,d:"medium",f:"Harper Lee's 1960 Pulitzer Prize-winning novel set in Depression-era Alabama."},
                {q:"What language did Dante Alighieri write 'The Divine Comedy' in?",o:["Latin","French","Spanish","Italian"],c:3,d:"hard",f:"Dante chose to write in the Tuscan vernacular rather than Latin, helping establish Italian as a literary language."},
                {q:"Who wrote 'War and Peace'?",o:["Fyodor Dostoevsky","Anton Chekhov","Ivan Turgenev","Leo Tolstoy"],c:3,d:"medium",f:"Tolstoy's epic novel, published in 1869, contains over 580,000 words and 559 named characters."},
                {q:"What is the name of Sherlock Holmes's address?",o:["10 Downing Street","221B Baker Street","17 Cherry Tree Lane","4 Privet Drive"],c:1,d:"easy",f:"The real 221B Baker Street is now the Sherlock Holmes Museum in London."},
                {q:"Who wrote 'The Catcher in the Rye'?",o:["Jack Kerouac","J.D. Salinger","Norman Mailer","Truman Capote"],c:1,d:"medium",f:"Salinger published it in 1951 but refused all adaptations of the novel during his lifetime."},
                {q:"Which book begins with the line 'Call me Ishmael'?",o:["The Old Man and the Sea","Lord Jim","Moby Dick","Heart of Darkness"],c:2,d:"medium",f:"One of the most famous opening lines in literature, from Herman Melville's 1851 novel."},
                {q:"Who wrote 'Don Quixote'?",o:["Gabriel Garcia Marquez","Pablo Neruda","Miguel de Cervantes","Jorge Luis Borges"],c:2,d:"medium",f:"Published in 1605, Don Quixote is often cited as the first modern novel."},
                {q:"In which fictional school does Harry Potter study?",o:["Beauxbatons","Durmstrang","Castelobruxo","Hogwarts"],c:3,d:"easy",f:"Hogwarts School of Witchcraft and Wizardry is located in the Scottish Highlands."}
            ],
            tech: [
                {q:"What does 'HTTP' stand for?",o:["HyperText Transfer Protocol","HighTech Transfer Program","HyperText Transmission Process","Host Transfer Text Protocol"],c:0,d:"easy",f:"HTTP is the foundation of data communication on the World Wide Web, created by Tim Berners-Lee."},
                {q:"Who co-founded Apple with Steve Jobs?",o:["Bill Gates","Paul Allen","Steve Wozniak","Jony Ive"],c:2,d:"easy",f:"Steve Wozniak designed the original Apple I computer in 1976 and built it by hand."},
                {q:"What does 'CPU' stand for?",o:["Central Processing Unit","Computer Power Unit","Core Program Utility","Central Program Uploader"],c:0,d:"easy",f:"The CPU executes instructions and is often called the brain of the computer."},
                {q:"Which company developed the Android operating system?",o:["Apple","Samsung","Microsoft","Google"],c:3,d:"easy",f:"Google acquired Android Inc. in 2005, and the first Android phone launched in 2008."},
                {q:"What does 'WWW' stand for?",o:["World Wide Web","World Web Wire","Wide World Web","Web World Wire"],c:0,d:"easy",f:"Tim Berners-Lee invented the World Wide Web in 1989 while working at CERN."},
                {q:"In computing, how many bits are in a byte?",o:["4","6","8","16"],c:2,d:"easy",f:"The byte was standardised at 8 bits in the 1960s; it can represent 256 different values."},
                {q:"Which programming language is known as the backbone of the web?",o:["Python","Java","C++","JavaScript"],c:3,d:"medium",f:"JavaScript runs in every web browser and has become the world's most widely used programming language."},
                {q:"What year was the first iPhone released?",o:["2005","2006","2007","2008"],c:2,d:"easy",f:"Steve Jobs unveiled the original iPhone on 9 January 2007 and it went on sale in June."},
                {q:"What does 'RAM' stand for?",o:["Random Access Memory","Read-Able Memory","Remote Access Module","Rapid Action Memory"],c:0,d:"easy",f:"RAM temporarily stores data the CPU is actively using; more RAM generally means better performance."},
                {q:"Who is considered the father of computing?",o:["Alan Turing","Charles Babbage","John von Neumann","Ada Lovelace"],c:1,d:"medium",f:"Charles Babbage designed the Analytical Engine in the 1830s, a precursor to modern computers."},
                {q:"What does 'AI' stand for in the tech context?",o:["Automated Integration","Artificial Intelligence","Advanced Interface","Adaptive Input"],c:1,d:"easy",f:"The term Artificial Intelligence was coined by John McCarthy at a 1956 Dartmouth Conference."},
                {q:"Which company created the Java programming language?",o:["Microsoft","Apple","IBM","Sun Microsystems"],c:3,d:"medium",f:"Sun Microsystems released Java in 1995; Oracle acquired Sun in 2010 and now maintains Java."},
                {q:"What is the name of the Linux mascot?",o:["Gnutella","Linus","Tux","Geeko"],c:2,d:"medium",f:"Tux the penguin was chosen by Linus Torvalds, who said he was bitten by a penguin as a child."},
                {q:"What does 'USB' stand for?",o:["Universal Serial Bus","Unified System Board","Universal System Base","Ultra Speed Bus"],c:0,d:"easy",f:"USB was developed in 1996 to standardise the connection of peripherals to personal computers."},
                {q:"Which language is primarily used for iOS app development?",o:["Java","Kotlin","Swift","Objective-C"],c:2,d:"medium",f:"Apple introduced Swift in 2014 as a faster, safer replacement for Objective-C."}
            ],
            food: [
                {q:"Which country is the origin of sushi?",o:["China","Korea","Thailand","Japan"],c:3,d:"easy",f:"Sushi originated in Japan, though fermented fish preservation methods came from Southeast Asia."},
                {q:"What is the main ingredient in guacamole?",o:["Tomato","Avocado","Lime","Onion"],c:1,d:"easy",f:"Guacamole originated with the Aztecs; the word comes from the Nahuatl ahuacamolli."},
                {q:"Which spice gives turmeric its distinctive yellow colour?",o:["Saffron","Capsaicin","Curcumin","Beta-carotene"],c:2,d:"hard",f:"Curcumin makes up 2-5% of turmeric and has been studied for anti-inflammatory properties."},
                {q:"What type of pastry is used in a croissant?",o:["Shortcrust","Choux","Filo","Puff"],c:3,d:"medium",f:"Croissants use laminated puff pastry with 729 layers of dough and butter in traditional recipes."},
                {q:"Which country produces the most coffee in the world?",o:["Colombia","Vietnam","Indonesia","Brazil"],c:3,d:"medium",f:"Brazil has been the world's top coffee producer for over 150 years, growing around a third of global supply."},
                {q:"What is the main ingredient in hummus?",o:["Lentils","Black beans","Chickpeas","Kidney beans"],c:2,d:"easy",f:"Hummus means chickpeas in Arabic; the full name is hummus bi tahini, meaning chickpeas with tahini."},
                {q:"Mozzarella cheese originally comes from which country?",o:["France","Spain","Greece","Italy"],c:3,d:"easy",f:"Traditional Mozzarella di Bufala is made from the milk of Italian water buffalo."},
                {q:"What is the hottest pepper in the world (as of 2023)?",o:["Carolina Reaper","Scorpion Pepper","Ghost Pepper","Pepper X"],c:3,d:"hard",f:"Pepper X was created by Ed Curlin and certified by Guinness in 2023 at over 2.69 million Scoville units."},
                {q:"Which vitamin is produced when your skin is exposed to sunlight?",o:["Vitamin A","Vitamin B12","Vitamin D","Vitamin K"],c:2,d:"medium",f:"UVB rays convert cholesterol in the skin to vitamin D3, essential for calcium absorption."},
                {q:"What is the name of the Italian dessert made from coffee-soaked biscuits and mascarpone?",o:["Panna cotta","Cannoli","Gelato","Tiramisu"],c:3,d:"easy",f:"Tiramisu means 'pick me up' in Italian, a nod to the energising effect of coffee and sugar."},
                {q:"Brie and Camembert originate from which country?",o:["Belgium","Switzerland","France","Netherlands"],c:2,d:"easy",f:"Both are soft, white-rinded cheeses from the Normandy and Brie regions of France."},
                {q:"Which nut is used to make marzipan?",o:["Walnut","Hazelnut","Pistachio","Almond"],c:3,d:"easy",f:"Marzipan is made from ground almonds and sugar, with origins possibly in the Middle East."},
                {q:"What is the world's most expensive spice by weight?",o:["Vanilla","Cardamom","Truffle","Saffron"],c:3,d:"medium",f:"Saffron costs up to $10,000 per kilogram — each crocus flower yields only 3 stigmas."},
                {q:"What type of meat is traditionally used in a Beef Wellington?",o:["Sirloin","Rump","Fillet","Rib-eye"],c:2,d:"medium",f:"Beef Wellington wraps a beef fillet in pate, duxelles (mushroom paste), and puff pastry."},
                {q:"Which country invented pizza?",o:["USA","France","Greece","Italy"],c:3,d:"easy",f:"Modern pizza as we know it was developed in Naples, Italy, in the 18th and 19th centuries."}
            ],
            nature: [
                {q:"What is the largest land animal on Earth?",o:["Hippopotamus","White rhinoceros","Giraffe","African elephant"],c:3,d:"easy",f:"African bush elephants can weigh up to 13,000 kg and stand 4 metres tall."},
                {q:"How long does a typical elephant pregnancy last?",o:["12 months","16 months","22 months","28 months"],c:2,d:"hard",f:"At 22 months, elephants have the longest gestation period of any land mammal."},
                {q:"What is the fastest land animal?",o:["Lion","Springbok","Pronghorn","Cheetah"],c:3,d:"easy",f:"Cheetahs can reach 112 km/h in short sprints and accelerate from 0 to 100 km/h in 3 seconds."},
                {q:"Which is the only continent with no native ant species?",o:["Antarctica","Greenland","Iceland","Arctic"],c:0,d:"medium",f:"Ants are found on every continent except Antarctica, where conditions are too extreme."},
                {q:"What percentage of the Earth's species live in the ocean?",o:["50%","60%","70%","80%"],c:3,d:"hard",f:"The oceans cover 71% of Earth's surface and contain an estimated 80% of all life."},
                {q:"What type of tree produces acorns?",o:["Beech","Chestnut","Elm","Oak"],c:3,d:"easy",f:"A single oak tree can produce 20,000 acorns per year and live for over 1,000 years."},
                {q:"Which mammal lays eggs?",o:["Koala","Wombat","Platypus","Echidna"],c:2,d:"medium",f:"Both the platypus and echidna lay eggs — they are the only two egg-laying mammals (monotremes)."},
                {q:"What is the term for animals that eat only plants?",o:["Carnivore","Omnivore","Insectivore","Herbivore"],c:3,d:"easy",f:"Herbivores have evolved specialised teeth and digestive systems for processing plant matter."},
                {q:"How do trees communicate with each other?",o:["Through sunlight","Via root networks and fungi","Using pheromones in leaves","Through sound vibrations"],c:1,d:"medium",f:"Trees share nutrients and warning signals via underground fungal networks called mycorrhizae."},
                {q:"What is the name of the process by which water moves up a plant?",o:["Diffusion","Osmosis","Transpiration","Capillary action"],c:2,d:"medium",f:"Transpiration pulls water up through xylem vessels as it evaporates from leaf surfaces."},
                {q:"Which is the world's largest living organism?",o:["Blue whale","Giant sequoia","Honey fungus","Great Barrier Reef"],c:2,d:"hard",f:"The Armillaria honey fungus in Oregon spans 2,385 acres and is estimated to be 8,000 years old."},
                {q:"How long can a snail sleep for?",o:["1 week","1 month","3 months","3 years"],c:3,d:"hard",f:"Snails can hibernate for up to 3 years in dry conditions to prevent dehydration."},
                {q:"What colour is a polar bear's skin?",o:["White","Grey","Pink","Black"],c:3,d:"medium",f:"Despite white fur, polar bears have black skin that absorbs heat from the sun efficiently."},
                {q:"Which bird has the largest wingspan?",o:["Bald eagle","Condor","Albatross","Pelican"],c:2,d:"medium",f:"The wandering albatross has a wingspan of up to 3.7 metres, the largest of any living bird."},
                {q:"What is a group of lions called?",o:["Pack","Herd","Pod","Pride"],c:3,d:"easy",f:"A pride typically consists of related females, their cubs, and a small number of adult males."}
            ],
            art: [
                {q:"Who painted the Mona Lisa?",o:["Michelangelo","Raphael","Donatello","Leonardo da Vinci"],c:3,d:"easy",f:"Da Vinci worked on the Mona Lisa from approximately 1503 to 1519."},
                {q:"In which museum is the Mona Lisa displayed?",o:["The Uffizi","The Prado","The Louvre","The Met"],c:2,d:"easy",f:"The Louvre in Paris has displayed the Mona Lisa since 1797 and receives 9 million visitors a year."},
                {q:"Who painted the Sistine Chapel ceiling?",o:["Raphael","Leonardo da Vinci","Botticelli","Michelangelo"],c:3,d:"easy",f:"Michelangelo painted it between 1508 and 1512 while lying on his back on scaffolding."},
""");
        sb.append("""
                {q:"Which artist cut off part of his own ear?",o:["Paul Gauguin","Georges Seurat","Claude Monet","Vincent van Gogh"],c:3,d:"easy",f:"Van Gogh severed part of his left ear in December 1888 during a mental breakdown in Arles."},
                {q:"What art movement is Salvador Dali associated with?",o:["Cubism","Impressionism","Surrealism","Expressionism"],c:2,d:"medium",f:"Surrealism explored the unconscious mind; Dali's melting clocks in The Persistence of Memory are iconic."},
                {q:"Who sculpted 'The Thinker'?",o:["Michelangelo","Bernini","Brancusi","Auguste Rodin"],c:3,d:"medium",f:"Originally called The Poet, Rodin intended The Thinker to represent Dante contemplating The Divine Comedy."},
                {q:"Which Dutch artist is famous for painting sunflowers?",o:["Rembrandt","Johannes Vermeer","Piet Mondrian","Vincent van Gogh"],c:3,d:"easy",f:"Van Gogh painted several sunflower series to decorate his studio in Arles for Gauguin's visit."},
                {q:"What technique involves applying thick paint to create texture?",o:["Fresco","Chiaroscuro","Impasto","Sfumato"],c:2,d:"hard",f:"Impasto comes from the Italian for dough or paste; Van Gogh and Rembrandt used it extensively."},
                {q:"Who painted 'Girl with a Pearl Earring'?",o:["Rembrandt","Pieter de Hooch","Jan Steen","Johannes Vermeer"],c:3,d:"medium",f:"Vermeer's 1665 painting is sometimes called the Mona Lisa of the North."},
                {q:"Which ancient wonder of the world was a giant statue?",o:["Temple of Artemis","Mausoleum at Halicarnassus","Colossus of Rhodes","Statue of Zeus"],c:2,d:"medium",f:"The Colossus of Rhodes stood about 33 metres tall at the harbour entrance."},
                {q:"What is the name of the famous prehistoric cave paintings site in France?",o:["Altamira","Chauvet","Blombos","Lascaux"],c:3,d:"medium",f:"The Lascaux caves contain over 600 paintings of animals estimated to be 17,000 years old."},
                {q:"Which art movement is Pablo Picasso most associated with?",o:["Fauvism","Dadaism","Cubism","Futurism"],c:2,d:"medium",f:"Cubism, co-founded by Picasso and Braque, showed subjects from multiple viewpoints simultaneously."},
                {q:"Who painted 'The Scream'?",o:["Ernst Ludwig Kirchner","August Macke","Edvard Munch","Emil Nolde"],c:2,d:"medium",f:"Munch created four versions of The Scream; the 1895 pastel sold for $119.9 million in 2012."},
                {q:"What nationality was Frida Kahlo?",o:["Colombian","Argentinian","Cuban","Mexican"],c:3,d:"easy",f:"Kahlo created 143 paintings, 55 of which were self-portraits exploring identity and pain."},
                {q:"What type of art involves arranging found objects?",o:["Collage","Assemblage","Montage","Bricolage"],c:1,d:"hard",f:"Assemblage art was popularised in the 1950s and 60s by artists like Robert Rauschenberg and Louise Nevelson."}
            ],
            maths: [
                {q:"What is Pi approximately equal to?",o:["3.14","3.41","3.12","3.16"],c:0,d:"easy",f:"Pi (π) is the ratio of a circle's circumference to its diameter — an infinite non-repeating decimal."},
                {q:"What is the next prime number after 7?",o:["8","9","10","11"],c:3,d:"easy",f:"Prime numbers are divisible only by 1 and themselves; 11 is the 5th prime number."},
                {q:"What is 15% of 200?",o:["25","30","35","40"],c:1,d:"easy",f:"To find 15%, multiply by 0.15: 200 × 0.15 = 30."},
                {q:"How many degrees are in a right angle?",o:["45","60","90","120"],c:2,d:"easy",f:"A right angle is exactly one quarter of a full 360-degree rotation."},
                {q:"What is the sum of the interior angles of a triangle?",o:["90°","120°","180°","360°"],c:2,d:"easy",f:"This holds for all triangles in flat (Euclidean) geometry, regardless of shape or size."},
                {q:"What does the Pythagorean theorem state?",o:["a+b=c","a²+b²=c²","a×b=c²","a²×b²=c"],c:1,d:"medium",f:"Named after Pythagoras, though it was known to Babylonian mathematicians 1,000 years earlier."},
                {q:"What is the value of 2 to the power of 10?",o:["512","1000","1024","2048"],c:2,d:"medium",f:"2¹⁰ = 1024, which is why computer storage uses 1,024 bytes per kilobyte."},
                {q:"What is the Fibonacci sequence?",o:["1,2,4,8,16","1,1,2,3,5,8","2,4,6,8,10","1,3,5,7,9"],c:1,d:"medium",f:"Each number is the sum of the two preceding ones; it appears throughout nature in spirals and growth patterns."},
                {q:"What is 12 factorial (12!)?",o:["479,001,600","120,000,000","524,288,000","362,880,000"],c:0,d:"hard",f:"12! = 479,001,600; factorials grow extremely rapidly — 20! has 19 digits."},
                {q:"In statistics, what does the 'median' refer to?",o:["The average","The most common value","The middle value","The range"],c:2,d:"medium",f:"The median is less affected by extreme outliers than the mean, making it useful for income data."},
                {q:"What is the square root of 256?",o:["14","15","16","17"],c:2,d:"medium",f:"16 × 16 = 256; this makes 256 a perfect square. It's also 2 to the power of 8."},
                {q:"How many sides does a dodecagon have?",o:["10","11","12","13"],c:2,d:"medium",f:"Dodeca comes from Greek meaning twelve; a regular dodecagon has interior angles of 150 degrees each."},
                {q:"What is the only even prime number?",o:["0","1","2","4"],c:2,d:"medium",f:"2 is prime because it is only divisible by 1 and itself; all other even numbers are divisible by 2."},
                {q:"What is the name of the mathematical symbol ∞?",o:["Omega","Sigma","Phi","Infinity"],c:3,d:"easy",f:"The infinity symbol was introduced by mathematician John Wallis in 1655."},
                {q:"What is 17 × 13?",o:["201","211","221","231"],c:2,d:"medium",f:"17 × 13 = 221. A quick way: 17 × 13 = (15+2)(15-2) = 225 - 4 = 221."}
            ],
            space: [
                {q:"Which planet is known as the Red Planet?",o:["Venus","Mercury","Jupiter","Mars"],c:3,d:"easy",f:"Mars gets its reddish colour from iron oxide (rust) covering its surface."},
                {q:"What is the name of our galaxy?",o:["Andromeda","Triangulum","Sombrero","Milky Way"],c:3,d:"easy",f:"The Milky Way contains an estimated 200-400 billion stars and is about 100,000 light-years wide."},
                {q:"How long does it take Earth to orbit the Sun?",o:["364 days","365.25 days","366 days","363 days"],c:1,d:"easy",f:"The extra 0.25 days is why we add a leap day every 4 years (with some exceptions)."},
                {q:"What is a light-year?",o:["The time light takes to cross a galaxy","The distance light travels in a year","The age of a star","The speed of light"],c:1,d:"easy",f:"One light-year is about 9.46 trillion kilometres."},
                {q:"Which planet has rings?",o:["Only Saturn","Jupiter and Saturn","Saturn and Uranus","Saturn, Uranus, Jupiter and Neptune"],c:3,d:"medium",f:"All four outer planets have ring systems, though only Saturn's are easily visible from Earth."},
                {q:"What is the closest star to Earth (after the Sun)?",o:["Sirius","Betelgeuse","Proxima Centauri","Vega"],c:2,d:"medium",f:"Proxima Centauri is 4.24 light-years away; a spacecraft at current speeds would take 70,000 years to reach it."},
                {q:"Who was the first human to walk on the Moon?",o:["Buzz Aldrin","Yuri Gagarin","John Glenn","Neil Armstrong"],c:3,d:"easy",f:"Armstrong stepped onto the Moon on 20 July 1969 and said his famous 'one small step' quote."},
                {q:"What is the name of NASA's Mars rover that landed in 2021?",o:["Curiosity","Opportunity","Spirit","Perseverance"],c:3,d:"medium",f:"Perseverance also carried the Ingenuity helicopter, the first aircraft to fly on another planet."},
                {q:"How many moons does Mars have?",o:["0","1","2","4"],c:2,d:"medium",f:"Phobos and Deimos are Mars's two small, potato-shaped moons, likely captured asteroids."},
                {q:"What is a black hole?",o:["A dark star","A collapsed star so dense light cannot escape","An empty void in space","A type of nebula"],c:1,d:"medium",f:"The first image of a black hole was captured in 2019 by the Event Horizon Telescope."},
                {q:"Which is the hottest planet in our solar system?",o:["Mercury","Mars","Venus","Jupiter"],c:2,d:"medium",f:"Venus is hotter than Mercury despite being farther from the Sun, due to its thick CO2 atmosphere trapping heat."},
                {q:"What causes a solar eclipse?",o:["Earth passes between Sun and Moon","Moon passes between Sun and Earth","Sun passes between Earth and Moon","Earth's shadow falls on Moon"],c:1,d:"easy",f:"It's a remarkable coincidence that the Moon appears exactly the same size as the Sun from Earth."},
                {q:"What is the name of the force that holds galaxies together?",o:["Dark energy","Strong nuclear force","Dark matter","Electromagnetic force"],c:2,d:"hard",f:"Dark matter makes up about 27% of the universe but has never been directly detected."},
                {q:"How many planets are in our solar system?",o:["7","8","9","10"],c:1,d:"easy",f:"Pluto was reclassified as a dwarf planet in 2006, reducing the count from 9 to 8."},
                {q:"What is the largest planet in our solar system?",o:["Saturn","Uranus","Neptune","Jupiter"],c:3,d:"easy",f:"Jupiter is so large that all other planets could fit inside it with room to spare."}
            ],
            mythology: [
                {q:"Who is the Greek god of the sea?",o:["Zeus","Hades","Ares","Poseidon"],c:3,d:"easy",f:"Poseidon wielded a trident and could cause earthquakes, earning the title 'Earth-Shaker'."},
                {q:"In Norse mythology, what is the name of the world tree?",o:["Bifrost","Yggdrasil","Mjolnir","Asgard"],c:1,d:"medium",f:"Yggdrasil is an immense ash tree connecting the nine worlds of Norse cosmology."},
                {q:"Who is the Roman equivalent of the Greek god Ares?",o:["Jupiter","Neptune","Mars","Mercury"],c:2,d:"medium",f:"Mars was actually more revered in Roman culture than Ares was in Greek — he was Rome's father of the nation."},
                {q:"In Egyptian mythology, who is the god of the dead?",o:["Ra","Horus","Set","Osiris"],c:3,d:"medium",f:"Osiris was murdered by Set, resurrected by Isis, and became ruler of the underworld."},
                {q:"What is the name of Thor's hammer?",o:["Gungnir","Gram","Mjolnir","Tyrfing"],c:2,d:"easy",f:"Mjolnir means 'crusher' in Old Norse; it was said to level mountains when thrown."},
                {q:"In Greek mythology, who flew too close to the Sun?",o:["Narcissus","Orpheus","Perseus","Icarus"],c:3,d:"easy",f:"Icarus ignored his father Daedalus's warnings and his wax wings melted, causing him to fall into the sea."},
                {q:"Who is the Japanese sun goddess?",o:["Izanami","Susanoo","Amaterasu","Tsukuyomi"],c:2,d:"hard",f:"Amaterasu hid in a cave after fighting with her brother, plunging the world into darkness until lured out."},
                {q:"In Greek mythology, what is the name of the three-headed dog guarding the Underworld?",o:["Hydra","Cerberus","Chimera","Scylla"],c:1,d:"easy",f:"Cerberus prevented the dead from leaving and the living from entering the Underworld uninvited."},
                {q:"Who is the trickster god in Norse mythology?",o:["Odin","Freyr","Tyr","Loki"],c:3,d:"easy",f:"Loki could shape-shift and was responsible for both helping and hindering the gods."},
                {q:"In Hindu mythology, how many arms does the goddess Durga have?",o:["4","6","8","10"],c:3,d:"hard",f:"Durga's ten arms each hold a different weapon given by the gods to help her fight the demon Mahishasura."},
                {q:"Who is the Greek goddess of wisdom?",o:["Aphrodite","Hera","Artemis","Athena"],c:3,d:"easy",f:"Athens is named after Athena; she won a contest with Poseidon by giving the city an olive tree."},
                {q:"In Arthurian legend, what is the name of King Arthur's sword?",o:["Durandal","Joyeuse","Excalibur","Curtana"],c:2,d:"easy",f:"Excalibur was given to Arthur by the Lady of the Lake, not pulled from a stone in the original tales."},
                {q:"In Greek mythology, who is the god of wine?",o:["Apollo","Hermes","Pan","Dionysus"],c:3,d:"easy",f:"Dionysus was also the god of fertility and theatre; Roman equivalent is Bacchus."},
                {q:"What did Pandora's box release upon the world?",o:["Fire","Disease only","Gold","All evils except hope"],c:3,d:"medium",f:"Hope (Elpis) remained in the box; the original Greek says pithos (jar) not box — a mistranslation by Erasmus."},
                {q:"Who is the Roman god of the Sun?",o:["Apollo","Sol Invictus","Helios","Aurora"],c:1,d:"hard",f:"Sol Invictus ('Unconquered Sun') was a major state religion in the late Roman Empire, distinct from Apollo."}
            ],
            pop: [
                {q:"Which TV show features the fictional town of Hawkins, Indiana?",o:["The OA","Dark","Wayward Pines","Stranger Things"],c:3,d:"easy",f:"Stranger Things debuted on Netflix in 2016 and draws heavily on 1980s pop culture."},
                {q:"Who played Walter White in Breaking Bad?",o:["Bryan Cranston","Aaron Paul","Dean Norris","Bob Odenkirk"],c:0,d:"easy",f:"Bryan Cranston won four Emmy Awards for Outstanding Lead Actor in a Drama Series for the role."},
                {q:"Which video game features a character named Master Chief?",o:["Gears of War","Call of Duty","Destiny","Halo"],c:3,d:"easy",f:"Master Chief is the Spartan-II supersoldier protagonist of the Halo franchise, created by Bungie."},
                {q:"What is the name of the coffee shop in Friends?",o:["Central Perk","The One Cup","Brew York","Java Joe"],c:0,d:"easy",f:"Central Perk is where the six friends spent most of their time across 10 seasons."},
                {q:"Who wrote and performed 'Rolling in the Deep'?",o:["Rihanna","Beyonce","Taylor Swift","Adele"],c:3,d:"easy",f:"Released in 2010, Rolling in the Deep spent 7 weeks at number 1 on the US Billboard Hot 100."},
                {q:"In which fictional universe does Spider-Man exist?",o:["DC Universe","Dark Horse","Image Comics","Marvel Universe"],c:3,d:"easy",f:"Spider-Man was created by Stan Lee and Steve Ditko, first appearing in Amazing Fantasy #15 in 1962."},
                {q:"Which country is the K-pop group BTS from?",o:["Japan","China","Thailand","South Korea"],c:3,d:"easy",f:"BTS debuted in 2013 and became the first Korean act to top the US Billboard Hot 100."},
                {q:"What is the name of Batman's butler?",o:["James","Edwin","Alfred","Herbert"],c:2,d:"easy",f:"Alfred Pennyworth has served the Wayne family since Bruce's birth and is his closest confidant."},
                {q:"Which actress plays Katniss Everdeen in The Hunger Games films?",o:["Shailene Woodley","Emma Stone","Jennifer Lawrence","Emily Browning"],c:2,d:"easy",f:"Jennifer Lawrence won an Academy Award for Silver Linings Playbook while filming Catching Fire."},
                {q:"Which streaming platform released The Crown?",o:["HBO","Disney+","Amazon Prime","Netflix"],c:3,d:"easy",f:"The Crown depicts the reign of Queen Elizabeth II and has aired since 2016."},
                {q:"What is the name of Tony Stark's AI assistant?",o:["Siri","FRIDAY","Cortana","J.A.R.V.I.S."],c:3,d:"medium",f:"J.A.R.V.I.S. stands for Just A Rather Very Intelligent System; it was later evolved into Vision."},
                {q:"Which show popularised the phrase 'Winter is Coming'?",o:["The Witcher","Vikings","Game of Thrones","The Last Kingdom"],c:2,d:"easy",f:"It's the motto of House Stark in Game of Thrones, adapted from George R.R. Martin's novels."},
                {q:"Who played Jack Dawson in the film Titanic?",o:["Brad Pitt","Matt Damon","Leonardo DiCaprio","Tom Cruise"],c:2,d:"easy",f:"DiCaprio's role opposite Kate Winslet launched him to global superstardom in 1997."},
                {q:"What colour pill does Neo take in The Matrix?",o:["Blue","Green","Red","White"],c:2,d:"easy",f:"The red pill reveals the truth of the simulation; the blue pill would have kept him oblivious."},
                {q:"Which fictional detective lives at 221B Baker Street?",o:["Hercule Poirot","Philip Marlowe","Sam Spade","Sherlock Holmes"],c:3,d:"easy",f:"Holmes was created by Arthur Conan Doyle and first appeared in A Study in Scarlet in 1887."}
            ],
            animals: [
                {q:"What is the fastest animal on Earth?",o:["Pronghorn","Sailfish","Peregrine falcon","Cheetah"],c:2,d:"medium",f:"The peregrine falcon reaches over 320 km/h in a dive, making it Earth's fastest animal."},
                {q:"How many legs does a spider have?",o:["6","8","10","12"],c:1,d:"easy",f:"Eight legs are what separates spiders (arachnids) from insects, which have six."},
                {q:"What is the largest fish in the world?",o:["Great white shark","Bluefin tuna","Oarfish","Whale shark"],c:3,d:"easy",f:"The whale shark can grow up to 18 metres long and filters tiny plankton through its gills."},
                {q:"Which animal has the longest lifespan?",o:["Giant tortoise","Bowhead whale","Ocean quahog clam","Greenland shark"],c:3,d:"hard",f:"Greenland sharks can live over 400 years; one female was estimated to be 392 years old."},
                {q:"What is a group of wolves called?",o:["Herd","Flock","Pack","Pride"],c:2,d:"easy",f:"A pack is led by an alpha pair, and members cooperate to raise pups and hunt prey."},
                {q:"Which animal has the largest brain relative to body size?",o:["Chimpanzee","Bottlenose dolphin","Human","Orca"],c:2,d:"hard",f:"Humans have the largest brain relative to body size, which is linked to advanced cognition."},
                {q:"What do you call a female deer?",o:["Ewe","Sow","Jenny","Doe"],c:3,d:"easy",f:"Doe is used for females of several species including deer, kangaroos, and rabbits."},
                {q:"Which insect is the most dangerous in the world?",o:["Tsetse fly","Fire ant","Mosquito","Asian hornet"],c:2,d:"medium",f:"Mosquitoes kill over 700,000 people annually through diseases like malaria, dengue, and Zika."},
                {q:"How many legs does a centipede have?",o:["50","Exactly 100","Always 44","It varies by species"],c:3,d:"hard",f:"Centipedes always have an odd number of pairs of legs, so can never have exactly 100."},
                {q:"What is the only mammal that cannot jump?",o:["Rhinoceros","Hippopotamus","Elephant","Sloth"],c:2,d:"medium",f:"Elephants never have all four feet off the ground simultaneously due to their size and skeletal structure."},
                {q:"Which bird can run the fastest?",o:["Rhea","Cassowary","Emu","Ostrich"],c:3,d:"medium",f:"Ostriches can run at 70 km/h and maintain 50 km/h for extended periods — faster than any other bird."},
                {q:"What is the name for a baby kangaroo?",o:["Kit","Pup","Cub","Joey"],c:3,d:"easy",f:"Joeys are born at about 2cm and spend 6-8 months developing further in the mother's pouch."},
                {q:"Which animal has the most teeth?",o:["Great white shark","Snail","Crocodile","Catfish"],c:1,d:"hard",f:"Garden snails have up to 14,175 teeth arranged in rows on a tongue-like organ called a radula."},
                {q:"What is the collective name for a group of flamingos?",o:["A colony","A flock","A flamboyance","A gaggle"],c:2,d:"medium",f:"A group of flamingos is called a flamboyance — perfectly fitting for these pink, theatrical birds."},
                {q:"Do fish sleep?",o:["Never","Always with eyes open","Yes, but differently to mammals","No, they rest but not sleep"],c:2,d:"medium",f:"Fish enter a resting state with reduced activity and metabolism, but many lack the brainwave patterns of sleep."}
            ],
            language: [
                {q:"What is the most widely spoken language in the world (total speakers)?",o:["Mandarin","Spanish","Hindi","English"],c:3,d:"medium",f:"English has over 1.5 billion total speakers when including second-language speakers."},
                {q:"How many official languages does the United Nations have?",o:["4","5","6","7"],c:2,d:"medium",f:"Arabic, Chinese, English, French, Russian, and Spanish are the UN's six official languages."},
                {q:"What does the word 'emoji' mean in Japanese?",o:["Tiny face","Picture character","Little smile","Feeling word"],c:1,d:"medium",f:"E (picture) + moji (character) — emojis were invented in Japan by Shigetaka Kurita in 1999."},
                {q:"Which language has the most words?",o:["French","German","Chinese","English"],c:3,d:"medium",f:"The Oxford English Dictionary contains over 600,000 definitions; the language adds around 1,000 words per year."},
                {q:"What is the shortest word in the English language?",o:["A","I","It","At"],c:0,d:"easy",f:"'A' is the shortest word — a single letter serving as the indefinite article."},
                {q:"What does 'cognate' mean in linguistics?",o:["A dead language","Two words with the same origin","A borrowed word","A made-up word"],c:1,d:"hard",f:"English 'night' and German 'Nacht' are cognates — both from Proto-Germanic naht."},
                {q:"What is the term for a word that reads the same forwards and backwards?",o:["Anagram","Palindrome","Homophone","Acronym"],c:1,d:"easy",f:"Examples: racecar, level, madam, kayak. The word palindrome comes from Greek meaning 'running back again'."},
                {q:"Which punctuation mark is used to show possession in English?",o:["Comma","Colon","Semicolon","Apostrophe"],c:3,d:"easy",f:"The apostrophe for possession evolved from dropping letters in possessive pronouns in early English."},
                {q:"What is a word that sounds like what it describes called?",o:["Alliteration","Onomatopoeia","Assonance","Synecdoche"],c:1,d:"medium",f:"Examples: buzz, crash, sizzle, meow. The term comes from Greek meaning 'word-making'."},
                {q:"In English, what is a collective noun for a group of crows?",o:["A gaggle","A parliament","A murder","A colony"],c:2,d:"medium",f:"A murder of crows — the sinister name likely comes from folklore associating crows with death."},
                {q:"Which letter appears most frequently in the English language?",o:["A","T","S","E"],c:3,d:"medium",f:"E is the most common letter in English, French, German, and several other languages."},
                {q:"What does the prefix 'mis-' mean?",o:["Badly or wrongly","Not","Before","Again"],c:0,d:"easy",f:"Mis- indicates wrongness: misuse, mislead, misunderstand. It comes from Old English and Germanic roots."},
                {q:"What is the term for words with the same pronunciation but different meanings?",o:["Synonyms","Antonyms","Homophones","Homographs"],c:2,d:"medium",f:"Homophones: there/their/they're, to/too/two, bear/bare. Homo = same, phone = sound in Greek."},
                {q:"What is the longest word in the English dictionary?",o:["Antidisestablishmentarianism","Pneumonoultramicroscopicsilicovolcanoconiosis","Supercalifragilisticexpialidocious","Floccinaucinihilipilification"],c:1,d:"hard",f:"At 45 letters, it refers to a lung disease caused by inhaling very fine ash or silica dust."},
                {q:"Which symbol represents 'and' in old English writing?",o:["@","#","&","$"],c:2,d:"medium",f:"The ampersand (&) is a ligature of the Latin 'et' meaning 'and'; its name comes from 'and per se and'."}
            ],
            politics: [
                {q:"Who was the first female Prime Minister of the United Kingdom?",o:["Theresa May","Angela Merkel","Golda Meir","Margaret Thatcher"],c:3,d:"easy",f:"Thatcher served as PM from 1979 to 1990, earning the nickname 'The Iron Lady'."},
                {q:"Which country has the world's oldest written constitution still in use?",o:["UK","France","Australia","USA"],c:3,d:"medium",f:"The US Constitution was ratified in 1788 and has been continuously operational since 1789."},
                {q:"What does 'NATO' stand for?",o:["North Atlantic Treaty Organization","National Alliance Treaty Organization","Northern Army Treaty Order","North American Treaty Organization"],c:0,d:"easy",f:"NATO was founded in 1949 with 12 member states; it now has 32 members."},
                {q:"Who was South Africa's first Black president?",o:["Thabo Mbeki","Cyril Ramaphosa","Walter Sisulu","Nelson Mandela"],c:3,d:"easy",f:"Mandela served as president from 1994 to 1999 after spending 27 years in prison."},
                {q:"Which city is the headquarters of the United Nations?",o:["Geneva","Vienna","Brussels","New York City"],c:3,d:"easy",f:"The UN headquarters is in Manhattan; the land was donated by John D. Rockefeller Jr. in 1946."},
                {q:"What is the name of the upper house of the UK Parliament?",o:["The Senate","The House of Lords","The Privy Council","The House of Commons"],c:1,d:"easy",f:"The House of Lords has over 800 members, mostly life peers appointed by the Prime Minister."},
                {q:"Who is considered the founder of modern Singapore?",o:["Goh Chok Tong","Tony Tan","S R Nathan","Lee Kuan Yew"],c:3,d:"medium",f:"Lee Kuan Yew served as Singapore's first PM from 1959 to 1990 and transformed it into a global financial hub."},
                {q:"What does the United Nations Security Council consist of?",o:["5 permanent members","10 permanent members","5 permanent + 10 rotating","5 permanent + 5 rotating"],c:2,d:"medium",f:"The P5 (USA, UK, France, Russia, China) have veto power; 10 non-permanent seats rotate every 2 years."},
                {q:"Which country was the first to give women the right to vote nationally?",o:["Australia","USA","UK","New Zealand"],c:3,d:"medium",f:"New Zealand granted women the right to vote in 1893, 27 years before the United States."},
                {q:"Who was the longest-serving leader of the Soviet Union?",o:["Lenin","Stalin","Khrushchev","Brezhnev"],c:1,d:"medium",f:"Stalin ruled from 1924 until his death in 1953 — nearly 29 years."},
                {q:"What is the name of Japan's parliament?",o:["Duma","Bundestag","Knesset","Diet"],c:3,d:"medium",f:"The National Diet (Kokkai) consists of two chambers: the House of Representatives and House of Councillors."},
                {q:"Which country has the most UNESCO World Heritage Sites?",o:["China","Spain","France","Italy"],c:3,d:"hard",f:"Italy leads with 58 UNESCO World Heritage Sites, ahead of China's 57."},
                {q:"What does the acronym 'EU' stand for?",o:["European Union","Euro Union","European Unit","Eurasian Union"],c:0,d:"easy",f:"The EU has 27 member states following the UK's departure (Brexit) in 2020."},
                {q:"Who was the first person to walk on the Moon?",o:["Buzz Aldrin","Yuri Gagarin","John Glenn","Neil Armstrong"],c:3,d:"easy",f:"Armstrong's first step on the Moon on 20 July 1969 was watched live by an estimated 600 million people."},
                {q:"Which country has the most Nobel Peace Prize winners?",o:["Germany","France","UK","USA"],c:3,d:"hard",f:"The USA has had the most Nobel Peace Prize laureates, with recipients including Jimmy Carter, Barack Obama, and many institutions."}
            ],
            games: [
                {q:"Which video game franchise features the character Link?",o:["Metroid","Fire Emblem","Zelda","Kirby"],c:2,d:"easy",f:"Many players confuse Link with Zelda — Link is the hero; Zelda is the princess."},
                {q:"In Minecraft, what material is needed to make the strongest tools?",o:["Gold","Iron","Diamond","Netherite"],c:3,d:"medium",f:"Netherite was added in the Nether Update (2020) and is stronger and more durable than diamond."},
                {q:"Which game features 'Battle Royale' gameplay with 100 players on an island?",o:["Call of Duty","PUBG","Apex Legends","Fortnite"],c:3,d:"easy",f:"Fortnite's Battle Royale mode launched in 2017 and reached 350 million registered players."},
                {q:"Who is the protagonist of the God of War series?",o:["Achilles","Leonidas","Kratos","Odysseus"],c:2,d:"easy",f:"Kratos is a Spartan warrior who becomes the God of War after killing Ares."},
                {q:"In which game do you collect Pokémon?",o:["Digimon World","Dragon Quest Monsters","Monster Hunter","Pokemon"],c:3,d:"easy",f:"Pokémon was created by Satoshi Tajiri and has become the highest-grossing media franchise ever."},
                {q:"What is the best-selling video game console of all time?",o:["Nintendo DS","Game Boy","PlayStation 2","PlayStation 4"],c:2,d:"medium",f:"The PS2 sold over 155 million units and had a library of over 4,000 games."},
                {q:"In Super Mario, what is the name of Mario's brother?",o:["Wario","Waluigi","Luigi","Toad"],c:2,d:"easy",f:"Luigi debuted in Mario Bros. (1983) and got his own game in Luigi's Mansion (2001)."},
                {q:"Which company makes the Xbox?",o:["Sony","Nintendo","Sega","Microsoft"],c:3,d:"easy",f:"Microsoft launched the original Xbox in 2001 to compete with Sony's PlayStation 2."},
                {q:"What is the name of the main villain in the Legend of Zelda series?",o:["Bowser","Ghirahim","Skull Kid","Ganon"],c:3,d:"easy",f:"Ganon (or Ganondorf in his human form) is the Gerudo wielder of the Triforce of Power."},
                {q:"In chess, which piece can only move diagonally?",o:["Rook","Knight","Queen","Bishop"],c:3,d:"easy",f:"Bishops are limited to one colour throughout the game; each player starts with a light-square and dark-square bishop."},
                {q:"What is the maximum level in the original Dungeons and Dragons?",o:["15","20","25","30"],c:1,d:"hard",f:"D&D 5th Edition caps at level 20, though older editions had different limits."},
                {q:"Which game introduced the battle royale genre?",o:["Fortnite","PUBG","H1Z1","DayZ"],c:1,d:"medium",f:"PUBG (2017) popularised and defined the modern battle royale genre, reaching 75 million copies sold."},
                {q:"In which game would you find the 'Konami Code'?",o:["Mega Man","Castlevania","Street Fighter","Contra"],c:3,d:"medium",f:"Up Up Down Down Left Right Left Right B A — entering it in Contra gave 30 extra lives."},
                {q:"What year was the first iPhone released?",o:["2005","2006","2007","2008"],c:2,d:"easy",f:"Steve Jobs announced it on 9 January 2007, calling it 'a revolutionary product that changes everything.'"},
                {q:"Which studio developed the Dark Souls series?",o:["Capcom","Konami","Square Enix","FromSoftware"],c:3,d:"medium",f:"FromSoftware's Hidetaka Miyazaki directed Dark Souls, defining the 'Soulslike' subgenre."}
            ]
        };

        // ── STATE ──────────────────────────────────────────────────────────────────
        let totalPoints    = 0;
        let bestStreakAll   = 0;
        let currentCat     = null;
        let difficulty     = 'all';
        let questions      = [];
        let qIndex         = 0;
        let sessionCorrect = 0;
        let sessionWrong   = 0;
        let streak         = 0;
        let bestStreak     = 0;
        let sessionPoints  = 0;
        let reviewItems    = [];
        let answered       = false;

        const CATEGORIES = [
            {id:'gk',name:'General Knowledge',emoji:'🌐',h:210},
            {id:'history',name:'History',emoji:'📜',h:35},
            {id:'science',name:'Science',emoji:'🔬',h:160},
            {id:'geography',name:'Geography',emoji:'🗺️',h:120},
            {id:'sports',name:'Sports',emoji:'🏅',h:0},
            {id:'movies',name:'Movies',emoji:'🎬',h:285},
            {id:'music',name:'Music',emoji:'🎵',h:300},
            {id:'literature',name:'Literature',emoji:'📚',h:50},
            {id:'tech',name:'Technology',emoji:'💻',h:195},
            {id:'food',name:'Food & Drink',emoji:'🍽️',h:20},
            {id:'nature',name:'Nature',emoji:'🌿',h:140},
            {id:'art',name:'Art & Culture',emoji:'🎨',h:320},
            {id:'maths',name:'Maths',emoji:'➗',h:230},
""");
        sb.append("""
            {id:'space',name:'Space',emoji:'🚀',h:240},
            {id:'mythology',name:'Mythology',emoji:'⚡',h:55},
            {id:'pop',name:'Pop Culture',emoji:'🎭',h:270},
            {id:'animals',name:'Animals',emoji:'🦁',h:95},
            {id:'language',name:'Words & Language',emoji:'🔤',h:185},
            {id:'politics',name:'World Leaders',emoji:'🏛️',h:340},
            {id:'games',name:'Video Games',emoji:'🎮',h:260}
        ];

        const bests = JSON.parse(localStorage.getItem('mooseyBests') || '{}');

        // Build grid
        const grid = document.getElementById('cat-grid');
        CATEGORIES.forEach(function(cat) {
            var div = document.createElement('div');
            div.className = 'cat-card';
            div.style.setProperty('--h', cat.h);
            div.dataset.hue = cat.h;
            div.innerHTML = '<span class="emoji">' + cat.emoji + '</span>' +
                '<div class="name">' + cat.name + '</div>' +
                '<div class="best">Best: <b>' + (bests[cat.id] || 0) + '</b> pts</div>';
            div.addEventListener('click', function() { startQuiz(cat); });
            grid.appendChild(div);
        });

        // Difficulty pills
        document.querySelectorAll('.diff-pill').forEach(function(pill) {
            pill.addEventListener('click', function() {
                document.querySelectorAll('.diff-pill').forEach(function(p) { p.classList.remove('active'); });
                pill.classList.add('active');
                difficulty = pill.dataset.diff;
            });
        });

        function showScreen(id) {
            document.querySelectorAll('.screen').forEach(function(s) { s.classList.remove('active'); });
            document.getElementById(id).classList.add('active');
        }

        function shuffle(arr) {
            var a = arr.slice();
            for (var i = a.length - 1; i > 0; i--) {
                var j = Math.floor(Math.random() * (i + 1));
                var tmp = a[i]; a[i] = a[j]; a[j] = tmp;
            }
            return a;
        }

        function startQuiz(cat) {
            currentCat = cat;
            qIndex = 0; sessionCorrect = 0; sessionWrong = 0;
            streak = 0; bestStreak = 0; sessionPoints = 0;
            reviewItems = []; answered = false;

            // Filter by difficulty and pick 10 random questions
            var pool = BANK[cat.id] || [];
            if (difficulty !== 'all') {
                var filtered = pool.filter(function(q) { return q.d === difficulty; });
                pool = filtered.length >= 5 ? filtered : pool; // fallback if not enough
            }
            questions = shuffle(pool).slice(0, 10);

            document.getElementById('quiz-cat-badge').innerHTML = cat.emoji + ' ' + cat.name;
            document.getElementById('streak-badge').classList.remove('visible');
            document.getElementById('progress-fill').style.width = '0%';
            showScreen('quiz-screen');
            renderQuestion();
        }

        function renderQuestion() {
            answered = false;
            if (qIndex >= questions.length) { showResults(); return; }
            var q = questions[qIndex];
            var pct = (qIndex / questions.length) * 100;
            document.getElementById('progress-fill').style.width = pct + '%';
            var letters = ['A','B','C','D'];
            var diffIcon = {easy:'🌱',medium:'🔥',hard:'💀'}[q.d] || '🎲';

            // Shuffle options (keeping track of correct answer)
            var opts = q.o.map(function(text, i) { return {text: text, orig: i}; });
            opts = shuffle(opts);
            var newCorrect = opts.findIndex(function(o) { return o.orig === q.c; });

            var optHtml = opts.map(function(opt, i) {
                return '<button class="opt-btn" onclick="choose(' + i + ',' + newCorrect + ',' + JSON.stringify(q).replace(/"/g,'&quot;') + ')">' +
                    '<span class="letter">' + letters[i] + '</span>' + opt.text + '</button>';
            }).join('');

            document.getElementById('quiz-body').innerHTML =
                '<div class="q-number">' + diffIcon + ' Question ' + (qIndex+1) + ' of ' + questions.length + '</div>' +
                '<div class="q-text">' + q.q + '</div>' +
                '<div class="options-grid">' + optHtml + '</div>' +
                '<div class="feedback-bar" id="fb"></div>' +
                '<button class="next-btn" id="next-btn" onclick="nextQuestion()">' +
                (qIndex+1 < questions.length ? 'Next Question →' : 'See Results 🏁') + '</button>';
        }

        function choose(idx, correctIdx, q) {
            if (answered) return;
            answered = true;
            var isCorrect = idx === correctIdx;
            var points = pointsFor(q.d, isCorrect);
            sessionPoints += points; totalPoints += points;
            document.getElementById('total-pts').textContent = totalPoints;

            if (isCorrect) {
                sessionCorrect++; streak++;
                if (streak > bestStreak) bestStreak = streak;
                if (streak > bestStreakAll) { bestStreakAll = streak; document.getElementById('best-streak-hdr').textContent = bestStreakAll; }
            } else { sessionWrong++; streak = 0; }

            var sb = document.getElementById('streak-badge');
            if (streak >= 2) { sb.classList.add('visible'); document.getElementById('streak-count').textContent = streak; }
            else { sb.classList.remove('visible'); }

            // Get the text of correct and chosen options from the DOM
            var btns = document.querySelectorAll('.opt-btn');
            var correctText = btns[correctIdx].textContent.trim().slice(1).trim();
            var chosenText  = btns[idx].textContent.trim().slice(1).trim();
            reviewItems.push({q: q.q, correct: correctText, chosen: chosenText, ok: isCorrect});

            btns.forEach(function(btn, i) {
                btn.disabled = true;
                if (i === correctIdx) btn.classList.add('correct');
                else if (i === idx && !isCorrect) btn.classList.add('wrong');
            });

            var fb = document.getElementById('fb');
            fb.classList.add('show', isCorrect ? 'correct' : 'wrong');
            if (isCorrect) {
                var bonus = streak >= 3 ? ' 🔥 Streak bonus!' : '';
                fb.innerHTML = '✅ <span><b>+' + points + ' pts</b>' + bonus + ' — ' + q.f + '</span>';
            } else {
                fb.innerHTML = '❌ <span><b>' + points + ' pts</b> — Correct answer: <b>' + correctText + '</b>. ' + q.f + '</span>';
            }
            document.getElementById('next-btn').classList.add('show');
        }

        function pointsFor(diff, correct) {
            var map = {easy:[100,-50],medium:[200,-100],hard:[350,-150]};
            var pair = map[diff] || [150,-75];
            var pts = correct ? pair[0] : pair[1];
            if (correct && streak >= 3) pts = Math.round(pts * 1.5);
            return pts;
        }

        function nextQuestion() { qIndex++; renderQuestion(); }

        function showResults() {
            document.getElementById('progress-fill').style.width = '100%';
            var pct = Math.round(sessionCorrect / questions.length * 100);
            document.getElementById('res-emoji').style.display  = 'none';
            document.getElementById('res-title').textContent = currentCat.emoji + ' ' + currentCat.name + ' Results';
            document.getElementById('res-score').textContent   = (sessionPoints>=0?'+':'')+sessionPoints;
            document.getElementById('res-correct').textContent = sessionCorrect;
            document.getElementById('res-wrong').textContent   = sessionWrong;
            document.getElementById('res-streak').textContent  = bestStreak;

            var prev = bests[currentCat.id] || 0;
            var bw = document.getElementById('best-badge-wrap');
            if (sessionPoints > prev) {
                bests[currentCat.id] = sessionPoints;
                localStorage.setItem('mooseyBests', JSON.stringify(bests));
                bw.innerHTML = '<div class="best-badge">🏆 New Personal Best!</div>';
            } else {
                bw.innerHTML = '<div style="color:var(--muted);font-size:13px;margin-top:6px">Previous best: ' + prev + ' pts</div>';
            }

            var rl = document.getElementById('review-list');
            rl.innerHTML = reviewItems.map(function(r) {
                return '<div class="review-item"><span class="review-icon">'+(r.ok?'✅':'❌')+'</span>' +
                    '<div><div class="review-q">'+r.q+'</div>' +
                    '<div class="review-a">'+(r.ok?r.correct:'You said: '+r.chosen+' · Correct: '+r.correct)+'</div></div></div>';
            }).join('');

            document.querySelectorAll('.cat-card').forEach(function(card) {
                var nameEl = card.querySelector('.name');
""");
        sb.append("""
                var cat = CATEGORIES.find(function(c) { return nameEl && nameEl.textContent === c.name; });
                if (cat) card.querySelector('.best b').textContent = bests[cat.id] || 0;
            });

            showScreen('results-screen');
        }

        function playAgainSame() { startQuiz(currentCat); }
        function goHome() { showScreen('category-screen'); }
        </script>
        </body>
        </html>

""");
        return sb.toString();
    }
}