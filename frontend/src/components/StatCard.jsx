function StatCard({ title, value, color }) {

  return (
    <div className="bg-white rounded-xl shadow-sm border p-5 flex flex-col gap-2">

      <span className="text-gray-500 text-sm">
        {title}
      </span>

      <span className={`text-2xl font-bold ${color}`}>
        {value}
      </span>

    </div>
  );

}

export default StatCard;