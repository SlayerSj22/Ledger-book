import { useEffect, useState } from "react";
import axios from "axios";
import AccountCard from "../components/AccountCard";

function AccountsPage() {

  const [accounts, setAccounts] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const size = 10;

  useEffect(() => {
    fetchAccounts(page);
  }, [page]);

  const fetchAccounts = async (pageNumber) => {

    try {

      const res = await axios.get(
        `http://localhost:8000/account/pending?page=${pageNumber}&size=${size}`
      );

      const pageData = res.data.data;

      setAccounts(pageData.content);
      setTotalPages(pageData.totalPages);

    } catch (err) {

      console.error("Error fetching accounts", err);

    }

  };

  return (
    <div className="max-w-6xl mx-auto p-6">

      <h1 className="text-2xl font-bold mb-6">
        Accounts
      </h1>

      {/* Horizontal Cards */}
      <div className="flex flex-col gap-4">

        {accounts.map((account) => (
          <AccountCard
            key={account.id}
            account={account}
            refresh={() => fetchAccounts(page)}
          />
        ))}

      </div>

      {/* Pagination */}
      <div className="flex justify-center items-center gap-4 mt-8">

        <button
          disabled={page === 0}
          onClick={() => setPage(page - 1)}
          className="px-4 py-2 border rounded disabled:opacity-40"
        >
          Prev
        </button>

        <span>
          Page {page + 1} / {totalPages}
        </span>

        <button
          disabled={page + 1 >= totalPages}
          onClick={() => setPage(page + 1)}
          className="px-4 py-2 border rounded disabled:opacity-40"
        >
          Next
        </button>

      </div>

    </div>
  );
}

export default AccountsPage;