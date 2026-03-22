import React, { useState, useEffect } from 'react';

const HeroSection = () => {
  const [step, setStep] = useState(0);

  useEffect(() => {
    // Sequenzsteuerung:
    // 0 = Nur "Teamarbeit" sichtbar
    // 1 = "Einfach" shaked
    // 2 = "Sicher" fällt runter
    // 3 = "Open Source" fadet ein
    
    const t1 = setTimeout(() => setStep(1), 500);
    const t2 = setTimeout(() => setStep(2), 1200);
    const t3 = setTimeout(() => setStep(3), 2500);

    return () => { clearTimeout(t1); clearTimeout(t2); clearTimeout(t3); };
  }, []);

  return (
    <section className="flex flex-col items-center justify-center text-center px-4 py-20 bg-white">
      
      {/* Badge oben */}
      <div className="mb-6 flex items-center gap-2 px-4 py-1.5 rounded-full bg-blue-50 text-blue-600 text-sm font-medium border border-blue-100">
        <span className="relative flex h-2 w-2">
          <span className="animate-ping absolute inline-flex h-full w-2 rounded-full bg-blue-400 opacity-75"></span>
          <span className="relative inline-flex rounded-full h-2 w-2 bg-blue-500"></span>
        </span>
        Aktiv in Entwicklung · v1.0
      </div>

      {/* Haupt-Überschrift */}
      <h1 className="text-5xl md:text-6xl font-bold text-slate-900 mb-4 tracking-tight">
        Teamarbeit organisieren.
      </h1>

      {/* Die animierte Zeile */}
      <h2 className="text-5xl md:text-6xl font-bold text-blue-600 mb-8 tracking-tight flex flex-wrap justify-center gap-4">
        
        {/* EINFACH */}
        <span className={step >= 1 ? "animate-shake inline-block" : "opacity-0"}>
          Einfach.
        </span>

        {/* SICHER */}
        <span className={step >= 2 ? "animate-drop-earthquake inline-block" : "opacity-0"}>
          Sicher.
        </span>

        {/* OPEN SOURCE */}
        <span className={step >= 3 ? "animate-fade-in inline-block" : "opacity-0"}>
          Open Source.
        </span>
      </h2>

      {/* Untertext (Dein Text vom Screenshot) */}
      <p className="max-w-2xl text-lg text-slate-500 mb-10 leading-relaxed">
        Boardly ist ein kollaboratives Kanban-Board mit echtem Organisations-Management, 
        rollenbasierter Zugriffskontrolle und JWT-Authentifizierung.
      </p>

      {/* Buttons */}
      <div className="flex flex-col sm:flex-row gap-4">
        <button className="px-8 py-3.5 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 transition-colors shadow-lg shadow-blue-200">
          Kostenlos starten
        </button>
        <button className="px-8 py-3.5 bg-white text-slate-700 font-semibold rounded-lg border border-slate-200 hover:bg-slate-50 transition-colors flex items-center gap-2">
           Quellcode ansehen
        </button>
      </div>

    </section>
  );
};

export default HeroSection;